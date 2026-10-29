package ir.rz.rzvanish;

import ir.rz.rzvanish.api.RZVanishAPI;
import ir.rz.rzvanish.command.VanishCommand;
import ir.rz.rzvanish.hook.PlaceholderHook;
import ir.rz.rzvanish.listener.PlayerListener;
import ir.rz.rzvanish.listener.VanishActionListener;
import ir.rz.rzvanish.manager.MessageManager;
import ir.rz.rzvanish.manager.PriorityManager;
import ir.rz.rzvanish.manager.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RZVanish extends JavaPlugin {

    private static RZVanish instance;
    private VanishManager vanishManager;
    private MessageManager messageManager;
    private PriorityManager priorityManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.messageManager = new MessageManager(this);
        this.priorityManager = new PriorityManager(this);
        this.vanishManager = new VanishManager(this);

        RZVanishAPI.init(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new VanishActionListener(this), this);

        VanishCommand cmd = new VanishCommand(this);
        getCommand("vanish").setExecutor(cmd);
        getCommand("vanish").setTabCompleter(cmd);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderHook(this).register();
            getLogger().info("Hooked into PlaceholderAPI.");
        }

        getLogger().info("RZVanish v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (vanishManager != null) vanishManager.cleanup();
        getLogger().info("RZVanish disabled.");
    }

    public void reload() {
        reloadConfig();
        messageManager.reload();
        priorityManager.reload();
    }

    public static RZVanish getInstance() { return instance; }
    public VanishManager getVanishManager() { return vanishManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public PriorityManager getPriorityManager() { return priorityManager; }
}
