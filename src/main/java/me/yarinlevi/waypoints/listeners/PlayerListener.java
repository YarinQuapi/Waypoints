package me.yarinlevi.waypoints.listeners;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import me.yarinlevi.waypoints.waypointData.FileManager;
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
    @Getter private final File waypointDataFile;
    @Getter private final FileConfiguration waypointData;


    public PlayerListener() {
        waypointDataFile = new File(Waypoints.getInstance().getDataFolder(), "waypointsData.yml");
        waypointData = YamlConfiguration.loadConfiguration(waypointDataFile);
        FileManager.registerwaypointData(waypointDataFile, waypointData);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> FileManager.save(waypointDataFile, waypointData), 0L, (300 * 20));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> loadPlayer(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> unloadPlayer(event.getPlayer().getUniqueId()));
    }

    public List<Waypoint> getPublicWaypointsFromwaypointData() {
        List<Waypoint> list = new ArrayList<>();

        if (waypointData.contains("public_waypoints")) {
            for (String uuid : waypointData.getConfigurationSection("public_waypoints").getKeys(false)) {
                for (String waypoint : waypointData.getConfigurationSection("public_waypoints." + uuid).getKeys(false)) {
                    ConfigurationSection waypointSection = waypointData.getConfigurationSection(uuid + ".waypoints." + waypoint);

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
        if (!waypointData.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
        }

        waypointData.set("public_waypoints." + uuid + "." + waypoint, null);
        waypointData.set("public_waypoints." + uuid + "." + name, "PUBLIC");
    }

    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws PlayerDoesNotExistException, WaypointDoesNotExistException {
        if (!waypointData.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
        }

        ConfigurationSection waypointSection = waypointData.getConfigurationSection(uuid.toString()).getConfigurationSection("waypoints");

        if (waypointSection.contains(waypoint)) {
            ConfigurationSection waypointConfiguration = waypointSection.getConfigurationSection(waypoint);

            waypointConfiguration.set("state", state);

            if (state.equals(WaypointState.PUBLIC)) {
                waypointData.set("public_waypoints." + uuid + "." + waypoint, "PUBLIC");
            } else if (state.equals(WaypointState.PRIVATE)) {
                waypointData.set("public_waypoints." + uuid + "." + waypoint, null);
            }

        } else throw new WaypointDoesNotExistException(Utils.newMessage(String.format("&cNo waypoint found with name: &f\"&d%s&f\"", waypoint)));
    }

    public void loadPlayer(UUID uuid) {
        if (!waypointData.contains(uuid.toString())) {
            waypointData.createSection(uuid.toString());
            waypointData.getConfigurationSection(uuid.toString()).createSection("waypoints");

            Waypoints.getInstance().getPlayerDataManager().insertPlayer(uuid, new ArrayList<>());
        } else {
            List<Waypoint> waypoints = new ArrayList<>();
            ConfigurationSection waypointSection = waypointData.getConfigurationSection(uuid.toString()).getConfigurationSection("waypoints");

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

            Waypoints.getInstance().getPlayerDataManager().insertPlayer(uuid, waypoints);
            Waypoints.getInstance().getLogger().info("Loaded waypoints waypointData for uuid: " + uuid);
        }
    }

    public void unloadPlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            Waypoints.getInstance().getActionBarTracker().unTrack(Bukkit.getPlayer(uuid));
        }

        PlayerData playerwaypointData = Waypoints.getInstance().getWaypointHandler().getPlayerwaypointData(uuid);

        ConfigurationSection playerSection = waypointData.getConfigurationSection(uuid.toString());

        if (!playerwaypointData.getWaypointList().isEmpty()) {
            ConfigurationSection waypointSection = playerSection.createSection("waypoints");

            for (Waypoint waypoint : playerwaypointData.getWaypointList()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
                waypointSection.set(waypoint.getName() + ".state", waypoint.getState().toString().toUpperCase());
            }

            FileManager.save(waypointDataFile, waypointData);
            Waypoints.getInstance().getLogger().info("Saved waypoints waypointData for uuid: " + uuid);
        } else {
            if (playerSection.contains("waypoints")) {
                waypointData.set(uuid.toString(), null);
            }
            FileManager.save(waypointDataFile, waypointData);
        }

        Waypoints.getInstance().getPlayerDataManager().removePlayer(uuid);
    }
}