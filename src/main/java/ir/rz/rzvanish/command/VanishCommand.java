package ir.rz.rzvanish.command;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VanishCommand implements CommandExecutor, TabCompleter {

    private final RZVanish plugin;

    public VanishCommand(RZVanish plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            if (!player.hasPermission("rzvanish.use")) {
                plugin.getMessageManager().send(player, "no-permission");
                return true;
            }
            plugin.getVanishManager().toggleVanish(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!player.hasPermission("rzvanish.reload")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                plugin.reload();
                plugin.getMessageManager().send(player, "reloaded");
                break;
            case "priority":
                if (!player.hasPermission("rzvanish.priority")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                if (args.length == 1) {
                    plugin.getMessageManager().send(player, "priority-self",
                            "{priority}", String.valueOf(plugin.getPriorityManager().getPriority(player)));
                } else {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        plugin.getMessageManager().send(player, "player-not-found", "{player}", args[1]);
                        return true;
                    }
                    plugin.getMessageManager().send(player, "priority-other",
                            "{player}", target.getName(),
                            "{priority}", String.valueOf(plugin.getPriorityManager().getPriority(target)));
                }
                break;
            case "list":
                if (!player.hasPermission("rzvanish.list")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                List<Player> list = plugin.getVanishManager().getVanishedPlayers();
                if (list.isEmpty()) {
                    plugin.getMessageManager().send(player, "list-empty");
                    return true;
                }
                player.sendMessage(plugin.getMessageManager().get("list-header"));
                for (Player v : list) {
                    String msg = plugin.getMessageManager().getRaw("list-entry")
                            .replace("{player}", v.getName())
                            .replace("{priority}", String.valueOf(plugin.getPriorityManager().getPriority(v)));
                    player.sendMessage(plugin.getMessageManager().color(msg));
                }
                break;
            case "status":
                if (!player.hasPermission("rzvanish.status")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    plugin.getMessageManager().send(player, "usage");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    plugin.getMessageManager().send(player, "player-not-found", "{player}", args[1]);
                    return true;
                }
                String key = plugin.getVanishManager().isVanished(target) ? "status-vanished" : "status-visible";
                plugin.getMessageManager().send(player, key, "{player}", target.getName());
                break;
            case "set":
                if (!player.hasPermission("rzvanish.others")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                if (args.length < 3) {
                    plugin.getMessageManager().send(player, "usage");
                    return true;
                }
                Player setTarget = Bukkit.getPlayerExact(args[1]);
                if (setTarget == null) {
                    plugin.getMessageManager().send(player, "player-not-found", "{player}", args[1]);
                    return true;
                }
                boolean on = args[2].equalsIgnoreCase("on");
                if (on) {
                    plugin.getVanishManager().vanish(setTarget);
                    plugin.getMessageManager().send(player, "vanished-other", "{player}", setTarget.getName());
                } else {
                    plugin.getVanishManager().unvanish(setTarget);
                    plugin.getMessageManager().send(player, "unvanished-other", "{player}", setTarget.getName());
                }
                break;
            case "toggle":
                if (!player.hasPermission("rzvanish.others")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                if (args.length < 2) {
                    plugin.getMessageManager().send(player, "usage");
                    return true;
                }
                Player toggleTarget = Bukkit.getPlayerExact(args[1]);
                if (toggleTarget == null) {
                    plugin.getMessageManager().send(player, "player-not-found", "{player}", args[1]);
                    return true;
                }
                plugin.getVanishManager().toggleVanish(toggleTarget);
                break;
            case "interact":
                if (!player.hasPermission("rzvanish.interact")) {
                    plugin.getMessageManager().send(player, "no-permission");
                    return true;
                }
                plugin.getVanishManager().toggleInteraction(player);
                break;
            default:
                plugin.getMessageManager().send(player, "usage");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "priority", "list", "status", "set", "toggle", "interact")
                    .stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length == 2 && Arrays.asList("status", "set", "toggle", "priority").contains(args[0].toLowerCase())) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            return Arrays.asList("on", "off");
        }
        return new ArrayList<>();
    }
}
