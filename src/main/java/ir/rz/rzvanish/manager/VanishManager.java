package ir.rz.rzvanish.manager;

import ir.rz.rzvanish.RZVanish;
import ir.rz.rzvanish.api.event.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public class VanishManager {

    private final RZVanish plugin;
    private final Set<UUID> vanished = new HashSet<>();
    private final Set<UUID> interactionEnabled = new HashSet<>();
    private final Map<UUID, Boolean> previousFly = new HashMap<>();

    public VanishManager(RZVanish plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(Player player) {
        return vanished.contains(player.getUniqueId());
    }

    public boolean canSee(Player viewer, Player target) {
        if (!isVanished(target)) return true;
        if (viewer.equals(target)) return true;
        if (viewer.hasPermission("rzvanish.see")) {
            int viewerPriority = plugin.getPriorityManager().getPriority(viewer);
            int targetPriority = plugin.getPriorityManager().getPriority(target);
            return viewerPriority >= targetPriority;
        }
        return false;
    }

    public boolean isInteractionEnabled(Player player) {
        return interactionEnabled.contains(player.getUniqueId());
    }

    public void toggleInteraction(Player player) {
        if (interactionEnabled.contains(player.getUniqueId())) {
            interactionEnabled.remove(player.getUniqueId());
            plugin.getMessageManager().send(player, "interact-off");
        } else {
            interactionEnabled.add(player.getUniqueId());
            plugin.getMessageManager().send(player, "interact-on");
        }
    }

    public void toggleVanish(Player player) {
        if (isVanished(player)) unvanish(player);
        else vanish(player);
    }

    public void vanish(Player player) {
        if (isVanished(player)) return;

        PrePlayerVanishEvent pre = new PrePlayerVanishEvent(player);
        Bukkit.getPluginManager().callEvent(pre);
        if (pre.isCancelled()) return;

        vanished.add(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!canSee(online, player) && !online.equals(player)) {
                online.hidePlayer(plugin, player);
            }
        }

        applyEffects(player);
        plugin.getMessageManager().send(player, "vanished");

        Bukkit.getPluginManager().callEvent(new PlayerVanishEvent(player));
    }

    public void unvanish(Player player) {
        if (!isVanished(player)) return;

        PrePlayerUnVanishEvent pre = new PrePlayerUnVanishEvent(player);
        Bukkit.getPluginManager().callEvent(pre);
        if (pre.isCancelled()) return;

        vanished.remove(player.getUniqueId());

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.showPlayer(plugin, player);
        }

        removeEffects(player);
        plugin.getMessageManager().send(player, "unvanished");

        Bukkit.getPluginManager().callEvent(new PlayerUnVanishEvent(player));
    }

    private void applyEffects(Player player) {
        List<String> effects = plugin.getConfig().getStringList("potion-effects");
        for (String entry : effects) {
            try {
                String[] parts = entry.split(":");
                PotionEffectType type = PotionEffectType.getByName(parts[0].toUpperCase());
                int amp = parts.length > 1 ? Integer.parseInt(parts[1]) - 1 : 0;
                if (type != null) player.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amp, false, false));
            } catch (Exception ignored) {}
        }
        if (plugin.getConfig().getBoolean("options.fly", true)) {
            previousFly.put(player.getUniqueId(), player.getAllowFlight());
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    private void removeEffects(Player player) {
        List<String> effects = plugin.getConfig().getStringList("potion-effects");
        for (String entry : effects) {
            try {
                PotionEffectType type = PotionEffectType.getByName(entry.split(":")[0].toUpperCase());
                if (type != null) player.removePotionEffect(type);
            } catch (Exception ignored) {}
        }
        if (plugin.getConfig().getBoolean("options.fly", true)
                && !plugin.getConfig().getBoolean("options.keep-fly-on-unvanish", false)) {
            Boolean prev = previousFly.remove(player.getUniqueId());
            if (prev != null && !prev) {
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }
    }

    public List<Player> getVanishedPlayers() {
        return vanished.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void cleanup() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isVanished(p)) unvanish(p);
        }
        vanished.clear();
        previousFly.clear();
        interactionEnabled.clear();
    }
}
