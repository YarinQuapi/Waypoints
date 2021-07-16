package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.Administration;
import me.yarinlevi.waypoints.commands.MainCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerListener;
import me.yarinlevi.waypoints.player.actionbar.ActionBarHandler;
import me.yarinlevi.waypoints.waypoint.WaypointHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author YarinQuapi
 */
public class Waypoints extends JavaPlugin {
    @Getter private static Waypoints instance;
    @Getter private String prefix;
    @Getter private WaypointHandler waypointHandler;
    @Getter private PlayerListener playerListener;
    @Getter private ActionBarHandler actionBarHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        registerConfigData();

        //New data methods

        playerListener = new PlayerListener();
        Bukkit.getPluginManager().registerEvents(playerListener, this);
        waypointHandler = new WaypointHandler();

        actionBarHandler = new ActionBarHandler();
        Bukkit.getPluginManager().registerEvents(actionBarHandler, this);

        this.getCommand("wpadmin").setExecutor(new Administration());
        this.getCommand("waypoint").setExecutor(new MainCommand());

        Bukkit.getOnlinePlayers().forEach(player -> playerListener.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui();

        Bukkit.getScheduler().runTaskTimer(this, () -> actionBarHandler.update(), 1L, 1L);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerListener.unloadPlayer(player.getUniqueId()));
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
    }
}