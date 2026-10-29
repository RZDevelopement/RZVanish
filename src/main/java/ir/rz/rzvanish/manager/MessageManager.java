package ir.rz.rzvanish.manager;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {

    private final RZVanish plugin;
    private String prefix;

    public MessageManager(RZVanish plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.prefix = color(plugin.getConfig().getString("messages.prefix", ""));
    }

    public String getRaw(String key) {
        return plugin.getConfig().getString("messages." + key, "&cMissing message: " + key);
    }

    public String get(String key) {
        return color(getRaw(key));
    }

    public void send(CommandSender sender, String key, String... replacements) {
        String msg = getRaw(key);
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        sender.sendMessage(prefix + color(msg));
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}