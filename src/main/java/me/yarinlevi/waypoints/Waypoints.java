package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.Administration;
import me.yarinlevi.waypoints.commands.MainCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerDeathListener;
import me.yarinlevi.waypoints.listeners.PlayerListener;
import me.yarinlevi.waypoints.player.trackers.ActionBarTracker;
import me.yarinlevi.waypoints.waypoint.WaypointHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private WaypointHandler waypointHandler;
    @Getter private PlayerListener playerListener;
    //@Getter private ActionBarHandler actionBarHandler;
    @Getter private ActionBarTracker actionBarTracker;

    private Map<Player, BukkitTask> tasks = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        Metrics metrics = new Metrics(this, 12124);

        playerListener = new PlayerListener();
        Bukkit.getPluginManager().registerEvents(playerListener, this);
        waypointHandler = new WaypointHandler();

        /*
        actionBarHandler = new ActionBarHandler();
        Bukkit.getPluginManager().registerEvents(actionBarHandler, this);
        */

        actionBarTracker = new ActionBarTracker();

        if (this.getConfig().getBoolean("DeathPoints")) {
            Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        }

        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());

        Bukkit.getOnlinePlayers().forEach(player -> playerListener.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui();

        //Bukkit.getScheduler().runTaskTimer(this, () -> actionBarHandler.update(), 1L, 1L);

        Bukkit.getScheduler().runTaskTimer(this, () -> actionBarTracker.update(), 10L, 10L);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerListener.unloadPlayer(player.getUniqueId()));
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
    }
}