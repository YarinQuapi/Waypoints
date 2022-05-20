package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.administration.AdminCommand;
import me.yarinlevi.waypoints.commands.waypoint.WaypointCommand;
import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.data.h2.H2DataManager;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerDeathListener;
import me.yarinlevi.waypoints.listeners.PlayerListener;
import me.yarinlevi.waypoints.player.PlayerDataManager;
import me.yarinlevi.waypoints.player.trackers.TrackerManager;
import me.yarinlevi.waypoints.utils.Constants;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.WaypointHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author YarinQuapi
 */
public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private WaypointHandler waypointHandler;
    @Getter private IData playerData;
    @Getter private TrackerManager trackerManager;
    @Getter private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        new Metrics(this, 12124);

        this.saveResource("messages.yml", false);
        new MessagesUtils();
        new Constants();

        playerData = new H2DataManager(); // Initialize SQLite

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        playerDataManager = new PlayerDataManager(); // loads player data manager

        waypointHandler = new WaypointHandler(); // loads waypoint handler

        trackerManager = new TrackerManager(); // loads tracking systems

        if (Constants.DEATH_POINTS) { // death point registerer
            Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
            getLogger().info(Utils.newMessageNoPrefix("&7> DeathPoints enabled!"));
        }

        // Reload Safe
        Bukkit.getOnlinePlayers().forEach(player -> playerData.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui(); // registers gui systems

        this.getCommand("waypointadmin").setExecutor(new AdminCommand());
        this.getCommand("waypoint").setExecutor(new WaypointCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerData.unloadPlayer(player.getUniqueId()));

        this.getPlayerData().closeDatabase();
    }
}