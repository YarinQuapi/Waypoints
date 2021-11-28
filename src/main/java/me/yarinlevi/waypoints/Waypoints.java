package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.Administration;
import me.yarinlevi.waypoints.commands.MainCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerDeathListener;
import me.yarinlevi.waypoints.listeners.PlayerListener;
import me.yarinlevi.waypoints.player.PlayerDataManager;
import me.yarinlevi.waypoints.player.trackers.TrackerManager;
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
@Plugin(name = "QWaypoints", version = "4.0B-1")
@Description(value = "A new way to store locations")
@Author(value = "Quapi")
@ApiVersion(value = ApiVersion.Target.v1_17)
@Commands()
public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private boolean deathPoints;
    @Getter private WaypointHandler waypointHandler;
    @Getter private PlayerListener playerListener;
    @Getter private TrackerManager trackerManager;
    @Getter private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        Metrics metrics = new Metrics(this, 12124);

        playerListener = new PlayerListener();
        Bukkit.getPluginManager().registerEvents(playerListener, this);

        playerDataManager = new PlayerDataManager();

        waypointHandler = new WaypointHandler();

        trackerManager = new TrackerManager();

        if (deathPoints) {
            Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
            getLogger().info(Utils.newMessageNoPrefix("&7> DeathPoints enabled!"));
        }

        Bukkit.getOnlinePlayers().forEach(player -> playerListener.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui();

        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerListener.unloadPlayer(player.getUniqueId()));
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
       deathPoints = getConfig().getBoolean("DeathPoints");
    }
}