package xyz.yarinlevi.waypoints;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.yarinlevi.waypoints.commands.Administration;
import xyz.yarinlevi.waypoints.commands.MainCommand;
import xyz.yarinlevi.waypoints.gui.GuiHandler;
import xyz.yarinlevi.waypoints.waypoint.PlayerListener;
import xyz.yarinlevi.waypoints.waypoint.WaypointHandler;

public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private String adminPrefix;
    @Getter private WaypointHandler waypointHandler;
    private PlayerListener playerListener;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        //New data methods

        playerListener = new PlayerListener();
        Bukkit.getPluginManager().registerEvents(playerListener, this);
        waypointHandler = new WaypointHandler();


        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());

        GuiHandler.registerGui().forEach((key, value) -> {
           Bukkit.getPluginManager().registerEvents(value, this);
        });

        Bukkit.getOnlinePlayers().forEach(player -> {
            playerListener.loadPlayer(player);
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            playerListener.unloadPlayer(player);
        });
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
       adminPrefix = getConfig().getString("AdminPrefix");
    }
}