package me.yarinlevi.waypoints.listeners;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.FileManager;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class PlayerListener implements Listener {
    private final File dataFile;
    @Getter private final FileConfiguration data;


    public PlayerListener() {
        dataFile = new File(Waypoints.getInstance().getDataFolder(), "waypointsData.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
        FileManager.registerData(dataFile, data);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> {

            Waypoints.getInstance().getLogger().info("Saving data...");
            FileManager.saveData(dataFile, data);
            Waypoints.getInstance().getLogger().info("Saved data.");

        }, 0L, (300 * 20));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> loadPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> unloadPlayer(event.getPlayer()));
    }

    public void loadPlayer(Player player) {
        if (!data.contains(player.getUniqueId().toString())) {
            data.createSection(player.getUniqueId().toString());
            data.getConfigurationSection(player.getUniqueId().toString()).createSection("waypoints");

            Waypoints.getInstance().getWaypointHandler().insertPlayer(player, new ArrayList<>());
        } else {
            List<Waypoint> waypoints = new ArrayList<>();
            ConfigurationSection waypointSection = data.getConfigurationSection(player.getUniqueId().toString()).getConfigurationSection("waypoints");

            ConfigurationSection waypoint;
            for (String key : waypointSection.getKeys(false)) {
                waypoint = waypointSection.getConfigurationSection(key);

                if (waypoint.getString("item").equals("DIRT")) {
                    waypoints.add(new Waypoint(player, key, (Location) waypoint.get("location"), waypoint.getBoolean("systemInduced")));
                } else {
                    waypoints.add(new Waypoint(player, key, (Location) waypoint.get("location"), new ItemStack(Material.getMaterial(waypoint.getString("item").toUpperCase())), waypoint.getBoolean("systemInduced")));
                }
            }

            Waypoints.getInstance().getWaypointHandler().insertPlayer(player, waypoints);
            Waypoints.getInstance().getLogger().info("Loaded waypoints data for player: " + player.getName());
        }
    }

    public void unloadPlayer(Player player) {
        PlayerData playerData = Waypoints.getInstance().getWaypointHandler().getPlayerData(player);

        ConfigurationSection playerSection = data.getConfigurationSection(player.getUniqueId().toString());

        if (!playerData.getWaypointList().isEmpty()) {
            ConfigurationSection waypointSection = playerSection.createSection("waypoints");

            for (Waypoint waypoint : playerData.getWaypointList()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
            }

            FileManager.saveData(dataFile, data);
            Waypoints.getInstance().getLogger().info("Saved waypoints data for player: " + player.getName());
        }

        Waypoints.getInstance().getWaypointHandler().removePlayer(player);
    }
}