package me.yarinlevi.waypoints.data.yaml;

import lombok.Getter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.data.helpers.FileUtils;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author YarinQuapi
 * @since 1.0
 * @deprecated YAML storage will be fully abandoned in v5.0
 **/
@Deprecated(forRemoval = true)
public class YamlDataManager implements IData {
    @Getter private final File waypointDataFile;
    @Getter private final FileConfiguration waypointData;

    public YamlDataManager() {
        waypointDataFile = new File(Waypoints.getInstance().getDataFolder(), "waypoints.yml");
        waypointData = YamlConfiguration.loadConfiguration(waypointDataFile);

        FileUtils.registerData(waypointDataFile, waypointData);

        Bukkit.getScheduler().runTaskTimerAsynchronously(Waypoints.getInstance(), () -> FileUtils.save(waypointDataFile, waypointData), 0L, (300 * 20));
    }

    @Override
    public List<Waypoint> getPublicWaypoints() {
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

    @Override
    public void renamePublicWaypoint(UUID uuid, String waypoint, String name) throws PlayerDoesNotExistException {
        if (!waypointData.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(MessagesUtils.getMessage("action_failed_not_found" , waypoint));
        }

        waypointData.set("public_waypoints." + uuid + "." + waypoint, null);
        waypointData.set("public_waypoints." + uuid + "." + name, "PUBLIC");
    }

    @Override
    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws PlayerDoesNotExistException, WaypointDoesNotExistException {
        if (!waypointData.contains(uuid.toString())) {
            throw new PlayerDoesNotExistException(MessagesUtils.getMessage("action_failed_not_found" , waypoint));
        }

        ConfigurationSection waypointSection = waypointData.getConfigurationSection(uuid.toString()).getConfigurationSection("waypoints");

        if (waypointSection.contains(waypoint)) {
            ConfigurationSection waypointConfiguration = waypointSection.getConfigurationSection(waypoint);

            waypointConfiguration.set("state", state.name());

            if (state.equals(WaypointState.PUBLIC)) {
                waypointData.set("public_waypoints." + uuid + "." + waypoint, "PUBLIC");
            } else if (state.equals(WaypointState.PRIVATE)) {
                waypointData.set("public_waypoints." + uuid + "." + waypoint, null);

                if (waypointData.getConfigurationSection("public_waypoints." + uuid).getKeys(false).isEmpty()) {
                    waypointData.set("public_waypoints." + uuid, null);
                }
            }

            this.saveFile();
        } else throw new WaypointDoesNotExistException(MessagesUtils.getMessage("action_failed_not_found" , waypoint));
    }

    @Override
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
            Waypoints.getInstance().getPlayerDataManager().loadPlayerSettings(uuid);

            Waypoints.getInstance().getLogger().info("Loaded waypoints waypointData for uuid: " + uuid);
        }
    }

    @Override
    public void unloadPlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null) {
            Waypoints.getInstance().getTrackerManager().unTrack(Bukkit.getPlayer(uuid));
        }

        this.savePlayer(uuid);

        Waypoints.getInstance().getPlayerDataManager().unloadPlayerSettings(uuid);
    }

    @Override
    public void savePlayer(UUID uuid) {
        PlayerData playerData = Waypoints.getInstance().getWaypointHandler().getPlayerData(uuid);

        ConfigurationSection playerSection = waypointData.getConfigurationSection(uuid.toString());

        if (!playerData.getWaypointList().isEmpty()) {
            ConfigurationSection waypointSection = playerSection.createSection("waypoints");

            for (Waypoint waypoint : playerData.getWaypointList()) {
                waypointSection.set(waypoint.getName() + ".location", waypoint.getLocation());
                waypointSection.set(waypoint.getName() + ".systemInduced", waypoint.isSystemInduced());
                waypointSection.set(waypoint.getName() + ".item", waypoint.getItem().getType().name());
                waypointSection.set(waypoint.getName() + ".state", waypoint.getState().toString().toUpperCase());
            }

            FileUtils.save(waypointDataFile, waypointData);
            Waypoints.getInstance().getLogger().info("Saved waypoints waypointData for uuid: " + uuid);
        } else {
            if (playerSection.contains("waypoints")) {
                waypointData.set(uuid.toString(), null);
            }
            FileUtils.save(waypointDataFile, waypointData);
        }
    }

    @Override
    public void saveFile() {
        FileUtils.save(waypointDataFile, waypointData);
    }
}
