package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.Administration;
import me.yarinlevi.waypoints.commands.MainCommand;
import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.data.h2.H2DataManager;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerDeathListener;
import me.yarinlevi.waypoints.listeners.PlayerListener;
import me.yarinlevi.waypoints.player.PlayerDataManager;
import me.yarinlevi.waypoints.player.trackers.TrackerManager;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.WaypointHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

/**
 * @author YarinQuapi
 */
@Plugin(name = "QWaypoints", version = "5.0-Early-Alpha")
@Description(value = "A new way to store locations")
@Author(value = "Quapi")
@ApiVersion(value = ApiVersion.Target.v1_18)
@Commands()
public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private boolean deathPoints;
    @Getter private WaypointHandler waypointHandler;
    @Getter private IData playerData;
    @Getter private TrackerManager trackerManager;
    @Getter private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        new Metrics(this, 12124);

        this.saveResource("messages.yml", false);
        new MessagesUtils();

        playerData = new H2DataManager(); // Initialize SQLite

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        playerDataManager = new PlayerDataManager(); // loads player data manager

        waypointHandler = new WaypointHandler(); // loads waypoint handler

        trackerManager = new TrackerManager(); // loads tracking systems

        if (deathPoints) { // death point registerer
            Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
            getLogger().info(Utils.newMessageNoPrefix("&7> DeathPoints enabled!"));
        }

        // Reload Safe
        Bukkit.getOnlinePlayers().forEach(player -> playerData.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui(); // registers gui systems

        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerData.unloadPlayer(player.getUniqueId()));

        this.getPlayerData().closeDatabase();
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
       deathPoints = getConfig().getBoolean("DeathPoints");
    }
}