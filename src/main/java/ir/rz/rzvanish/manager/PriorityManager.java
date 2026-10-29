package ir.rz.rzvanish.manager;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.entity.Player;

public class PriorityManager {

    private final RZVanish plugin;
    private boolean enabled;
    private int defaultPriority;

    public PriorityManager(RZVanish plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.enabled = plugin.getConfig().getBoolean("priorities.enabled", true);
        this.defaultPriority = plugin.getConfig().getInt("priorities.default", 5);
    }

    public int getPriority(Player player) {
        if (!enabled) return defaultPriority;
        for (int i = 5; i >= 1; i--) {
            if (player.hasPermission("rzvanish.priority." + i)) return i;
        }
        return defaultPriority;
    }
}
