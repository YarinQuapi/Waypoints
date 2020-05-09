package xyz.yarinlevi.waypoints;

import xyz.yarinlevi.waypoints.commands.Administration;
import xyz.yarinlevi.waypoints.commands.Commands2;
import xyz.yarinlevi.waypoints.data.WaypointManager2;
import xyz.yarinlevi.waypoints.events.Events;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Waypoints extends JavaPlugin {
    public static Waypoints instance;
    private Logger logger = this.getLogger();
    public static String lprefix;
    public static String ladminprefix;

    @Override
    public void onEnable() {
        Waypoints.instance = this;
        logger.info((lprefix) + "Loading the plugin...");
        System.out.println(this.getName() + " Registering config data...");
        this.saveDefaultConfig();
        registerConfigData();
        System.out.println(this.getName() + " Config data registered successfully.");
        System.out.println(this.getName() + " Loading Waypoints data...");
        WaypointManager2.waypointFile = new File(this.getDataFolder(), "Waypoints.yml");
        WaypointManager2.playerData = (FileConfiguration) YamlConfiguration.loadConfiguration(WaypointManager2.waypointFile);
        if (!WaypointManager2.waypointFile.exists()) {
            WaypointManager2.waypointFile.getParentFile().mkdirs();
        }
        else {
            try {
                WaypointManager2.playerData.load(WaypointManager2.waypointFile);
            }
            catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
        WaypointManager2.reloadWaypoints();
        System.out.println(this.getName() + " Waypoints loaded successfully.");
        this.getCommand("waypoint").setExecutor((CommandExecutor)new Commands2());
        this.getCommand("wpadmin").setExecutor((CommandExecutor)new Administration());
        this.getCommand("wpadmin").setPermission("waypoints.admin");
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        logger.info((lprefix) + "Plugin has been loaded!");
    }

    @Override
    public void onDisable() {
        logger.info("Disabling Waypoints...");
    }

    public static void registerConfigData() {
        Waypoints.lprefix = Waypoints.instance.getConfig().getString("Prefix");
    }
}
