package me.yarinlevi.waypoints;

import lombok.Getter;
import me.yarinlevi.waypoints.commands.Administration;
import me.yarinlevi.waypoints.commands.MainCommand;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.listeners.PlayerListener;
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

        Bukkit.getOnlinePlayers().forEach(player -> playerListener.loadPlayer(player.getUniqueId()));

        GuiUtils.registerGui();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> playerListener.unloadPlayer(player.getUniqueId()));
    }

    public void registerConfigData() {
       prefix = getConfig().getString("Prefix");
    }
}