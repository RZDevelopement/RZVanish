package ir.rz.rzvanish.listener;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final RZVanish plugin;

    public PlayerListener(RZVanish plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (plugin.getVanishManager().isVanished(online) && !plugin.getVanishManager().canSee(player, online)) {
                player.hidePlayer(plugin, online);
            }
        }

        if (plugin.getConfig().getBoolean("vanish-on-join", false)
                && player.hasPermission("rzvanish.use")) {
            plugin.getVanishManager().vanish(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getVanishManager().isVanished(player)
                && plugin.getConfig().getBoolean("fake-join-quit.enabled", true)) {
            event.quitMessage(null);
        }
    }
}
