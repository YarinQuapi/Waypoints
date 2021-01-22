package xyz.yarinlevi.waypoints.waypoint;

import lombok.Getter;
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
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.FileManager;

import java.io.File;
import java.util.HashMap;

public class PlayerListener implements Listener {
    private final File dataFile;
    @Getter private final FileConfiguration data;


    public PlayerListener() {
        dataFile = new File(Waypoints.getInstance().getDataFolder(), "waypointsData.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
        FileManager.registerData(dataFile, data);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().getLogger().info("Saving data...");
                FileManager.saveData(dataFile, data);
                Bukkit.getServer().getLogger().info("Saved data.");
            }
        }, 0L, (300 * 20));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!data.contains(player.getUniqueId().toString())) {
            data.createSection(player.getUniqueId().toString());
            data.getConfigurationSection(player.getUniqueId().toString()).createSection("waypoints");

            Waypoints.getInstance().getWaypointHandler().insertPlayer(player, new HashMap<>());
        } else {
            HashMap<String, Waypoint> waypoints = new HashMap<>();
            ConfigurationSection waypointSection = data.getConfigurationSection(player.getUniqueId().toString()).getConfigurationSection("waypoints");

            ConfigurationSection waypoint;
            for (String key : waypointSection.getKeys(false)) {
                waypoint = waypointSection.getConfigurationSection(key);

                if (waypoint.getString("item").equals("DIRT")) {
                    waypoints.put(key, new Waypoint(key, (Location) waypoint.get("location"), waypoint.getBoolean("systemInduced")));
                } else {
                    waypoints.put(key, new Waypoint(key, (Location) waypoint.get("location"), new ItemStack(Material.getMaterial(waypoint.getString("item").toUpperCase())), waypoint.getBoolean("systemInduced")));
                }
            }

            Waypoints.getInstance().getWaypointHandler().insertPlayer(player, waypoints);
            Bukkit.getServer().getLogger().info("Loaded waypoints data for player: " + player.getName());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        HashMap<String, Waypoint> waypoints = Waypoints.getInstance().getWaypointHandler().getWaypoints(player);

        ConfigurationSection playerSection = data.getConfigurationSection(player.getUniqueId().toString());

        if (!waypoints.isEmpty()) {
            ConfigurationSection waypointSection = playerSection.getConfigurationSection("waypoints");

            for (Waypoint waypoint : waypoints.values()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
            }
            FileManager.saveData(dataFile, data);
            Bukkit.getServer().getLogger().info("Saved waypoints data for player: " + player.getName());
        }

        Waypoints.getInstance().getWaypointHandler().removePlayer(player);
    }
}