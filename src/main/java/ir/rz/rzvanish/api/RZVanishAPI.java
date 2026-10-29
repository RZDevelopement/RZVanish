package ir.rz.rzvanish.api;

import ir.rz.rzvanish.RZVanish;
import org.bukkit.entity.Player;
import java.util.List;

public final class RZVanishAPI {
    private static RZVanish plugin;
    private RZVanishAPI() {}
    public static void init(RZVanish instance) { plugin = instance; }
    public static void vanishPlayer(Player p) { plugin.getVanishManager().vanish(p); }
    public static void unVanishPlayer(Player p) { plugin.getVanishManager().unvanish(p); }
    public static boolean isPlayerVanished(Player p) { return plugin.getVanishManager().isVanished(p); }
    public static boolean canSee(Player viewer, Player target) { return plugin.getVanishManager().canSee(viewer, target); }
    public static List<Player> getVanishedPlayers() { return plugin.getVanishManager().getVanishedPlayers(); }
}
