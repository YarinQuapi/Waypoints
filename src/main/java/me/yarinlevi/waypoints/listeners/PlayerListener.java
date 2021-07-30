package me.yarinlevi.waypoints.listeners;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.FileManager;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
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
    @Getter private final File dataFile;
    @Getter private final FileConfiguration data;


    public PlayerListener() {
        dataFile = new File(Waypoints.getInstance().getDataFolder(), "waypointsData.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
        FileManager.registerData(dataFile, data);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> FileManager.saveData(dataFile, data), 0L, (300 * 20));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> loadPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> unloadPlayer(event.getPlayer().getUniqueId()));
    }

    public List<Waypoint> getPublicWaypointsFromData() {
        List<Waypoint> list = new ArrayList<>();

        if (data.contains("public_waypoints")) {
            for (String uuid : data.getConfigurationSection("public_waypoints").getKeys(false)) {
                for (String waypoint : data.getConfigurationSection("public_waypoints." + uuid).getKeys(false)) {
                    ConfigurationSection waypointSection = data.getConfigurationSection(uuid + ".waypoints." + waypoint);

                    if (waypointSection.getString("item").equalsIgnoreCase("DIRT")) {
                        list.add(new Waypoint(UUID.fromString(uuid), waypoint, (Location) waypointSection.get("location"), WaypointState.PUBLIC, waypointSection.getBoolean("systemInduced")));
                    } else {
                        list.add(new Waypoint(UUID.fromString(uuid), waypoint, (Location) waypointSection.get("location"), new ItemStack(Material.getMaterial(waypointSection.getString("item").toUpperCase())), WaypointState.PUBLIC, waypointSection.getBoolean("systemInduced")));
                    }
                }
            }
        }

        return list;
    }

    public void renamePublicWaypoint(UUID uuid, String waypoint, String name) throws PlayerDoesNotExistException {
        if (!data.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
        }

        data.set("public_waypoints." + uuid + "." + waypoint, null);
        data.set("public_waypoints." + uuid + "." + name, "PUBLIC");
    }

    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws PlayerDoesNotExistException, WaypointDoesNotExistException {
        if (!data.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
        }

        ConfigurationSection waypointSection = data.getConfigurationSection(uuid.toString()).getConfigurationSection("waypoints");

        if (waypointSection.contains(waypoint)) {
            ConfigurationSection waypointConfiguration = waypointSection.getConfigurationSection(waypoint);

            waypointConfiguration.set("state", state);

            if (state.equals(WaypointState.PUBLIC)) {
                data.set("public_waypoints." + uuid + "." + waypoint, "PUBLIC");
            } else if (state.equals(WaypointState.PRIVATE)) {
                data.set("public_waypoints." + uuid + "." + waypoint, null);
            }

        } else throw new WaypointDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
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

                WaypointState state;

                if (waypoint.contains("state")) {
                    state = WaypointState.valueOf(waypoint.getString("state"));
                } else {
                    state = WaypointState.PRIVATE;
                }

                if (waypoint.getString("item").equals("DIRT")) {
                    waypoints.add(new Waypoint(uuid, key, (Location) waypoint.get("location"), state, waypoint.getBoolean("systemInduced")));
                } else {
                    waypoints.add(new Waypoint(uuid, key, (Location) waypoint.get("location"), new ItemStack(Material.getMaterial(waypoint.getString("item").toUpperCase())), state, waypoint.getBoolean("systemInduced")));
                }
            }

            Waypoints.getInstance().getWaypointHandler().insertPlayer(uuid, waypoints);
            Waypoints.getInstance().getLogger().info("Loaded waypoints data for uuid: " + uuid);
        }
    }

    public void unloadPlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            Waypoints.getInstance().getActionBarTracker().unTrack(Bukkit.getPlayer(uuid));
        }

        PlayerData playerData = Waypoints.getInstance().getWaypointHandler().getPlayerData(uuid);

        ConfigurationSection playerSection = data.getConfigurationSection(uuid.toString());

        if (!playerData.getWaypointList().isEmpty()) {
            ConfigurationSection waypointSection = playerSection.createSection("waypoints");

            for (Waypoint waypoint : playerData.getWaypointList()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
                waypointSection.set(waypoint.getName() + ".state", waypoint.getState().toString().toUpperCase());
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