package ir.rz.rzvanish.hook;

import ir.rz.rzvanish.RZVanish;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderHook extends PlaceholderExpansion {

    private final RZVanish plugin;

    public PlaceholderHook(RZVanish plugin) { this.plugin = plugin; }

    @Override public String getIdentifier() { return "rzvanish"; }
    @Override public String getAuthor() { return "RZ"; }
    @Override public String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) return "";
        if (params.equalsIgnoreCase("vanished")) {
            return plugin.getVanishManager().isVanished(player) ? "true" : "false";
        }
        if (params.equalsIgnoreCase("priority")) {
            return String.valueOf(plugin.getPriorityManager().getPriority(player));
        }
        if (params.equalsIgnoreCase("vanished_count")) {
            return String.valueOf(plugin.getVanishManager().getVanishedPlayers().size());
        }
        return null;
    }
}
