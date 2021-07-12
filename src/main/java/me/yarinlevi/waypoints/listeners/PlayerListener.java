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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> loadPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> unloadPlayer(event.getPlayer().getUniqueId()));
    }

    public void loadPlayer(UUID uuid) {
        if (!data.contains(uuid.toString())) {
            data.createSection(uuid.toString());
            data.getConfigurationSection(uuid.toString()).createSection("waypoints");

            Waypoints.getInstance().getWaypointHandler().insertPlayer(uuid, new ArrayList<>());
        } else {
            List<Waypoint> waypoints = new ArrayList<>();
            ConfigurationSection waypointSection = data.getConfigurationSection(uuid.toString()).getConfigurationSection("waypoints");

            ConfigurationSection waypoint;
            for (String key : waypointSection.getKeys(false)) {
                waypoint = waypointSection.getConfigurationSection(key);

                if (waypoint.getString("item").equals("DIRT")) {
                    waypoints.add(new Waypoint(uuid, key, (Location) waypoint.get("location"), waypoint.getBoolean("systemInduced")));
                } else {
                    waypoints.add(new Waypoint(uuid, key, (Location) waypoint.get("location"), new ItemStack(Material.getMaterial(waypoint.getString("item").toUpperCase())), waypoint.getBoolean("systemInduced")));
                }
            }

            Waypoints.getInstance().getWaypointHandler().insertPlayer(uuid, waypoints);
            Waypoints.getInstance().getLogger().info("Loaded waypoints data for uuid: " + uuid);
        }
    }

    public void unloadPlayer(UUID uuid) {
        PlayerData playerData = Waypoints.getInstance().getWaypointHandler().getPlayerData(uuid);

        ConfigurationSection playerSection = data.getConfigurationSection(uuid.toString());

        if (!playerData.getWaypointList().isEmpty()) {
            ConfigurationSection waypointSection = playerSection.createSection("waypoints");

            for (Waypoint waypoint : playerData.getWaypointList()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
            }

            FileManager.saveData(dataFile, data);
            Waypoints.getInstance().getLogger().info("Saved waypoints data for uuid: " + uuid);
        } else {
            if (playerSection.contains("waypoints")) {
                data.set(uuid.toString(), null);
            }
            FileManager.saveData(dataFile, data);
        }

        Waypoints.getInstance().getWaypointHandler().removePlayer(uuid);
    }
}