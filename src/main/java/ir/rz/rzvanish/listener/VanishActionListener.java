package ir.rz.rzvanish.listener;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class VanishActionListener implements Listener {

    private final RZVanish plugin;

    public VanishActionListener(RZVanish plugin) { this.plugin = plugin; }

    private boolean isBlocked(String key) {
        return plugin.getConfig().getBoolean("blocked-actions." + key, false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (isBlocked("block-break") && plugin.getVanishManager().isVanished(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (isBlocked("block-place") && plugin.getVanishManager().isVanished(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (isBlocked("interact") && plugin.getVanishManager().isVanished(p)
                && !plugin.getVanishManager().isInteractionEnabled(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p && isBlocked("attack-entities")
                && plugin.getVanishManager().isVanished(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p && isBlocked("receive-damage")
                && plugin.getVanishManager().isVanished(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p && isBlocked("pickup-items")
                && plugin.getVanishManager().isVanished(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isBlocked("drop-items") && plugin.getVanishManager().isVanished(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler
    public void onMobTarget(EntityTargetEvent e) {
        if (e.getTarget() instanceof Player p && isBlocked("mob-target")
                && plugin.getVanishManager().isVanished(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player p && isBlocked("hunger")
                && plugin.getVanishManager().isVanished(p)) {
            e.setCancelled(true);
        }
    }
}
