package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.FileUtils;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author YarinQuapi
 */
public class PlayerDataManager {
    @Getter private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    @Getter @Setter private File settingsFile;
    @Getter @Setter private FileConfiguration settingsData;

    public PlayerDataManager() {
        settingsFile = new File(Waypoints.getInstance().getDataFolder(), "playerSettings.yml");
        settingsData = YamlConfiguration.loadConfiguration(settingsFile);

        FileUtils.registerData(settingsFile, settingsData);
        FileUtils.save(settingsFile, settingsData);
    }

    public void loadPlayerSettings(UUID uuid) {
        PlayerData playerData = playerDataMap.get(uuid);

        if (settingsData.contains(uuid.toString())) {
            playerData.setPlayerDeathPoints(settingsData.getBoolean(uuid + ".deathpoints", true));
            playerData.setETracker(Waypoints.getInstance().getTrackerManager().getTracker(settingsData.getString(uuid + ".etracker", ETracker.ActionBar.getKey())).getETracker());
        }
    }

    public void unloadPlayerSettings(UUID uuid) {
        PlayerData playerData = playerDataMap.get(uuid);

        settingsData.set(uuid + ".deathpoints", playerData.isPlayerDeathPoints());
        settingsData.set(uuid + ".etracker", playerData.getETracker().getKey());

        FileUtils.save(settingsFile, settingsData);

        this.removePlayer(uuid);
    }

    public void insertPlayer(UUID uuid, List<Waypoint> waypoints) {
        if (!this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.put(uuid, new PlayerData(waypoints));
        }
    }

    public void removePlayer(UUID uuid) {
        if (this.playerDataMap.containsKey(uuid)) {
            this.playerDataMap.remove(uuid);
        }
    }
}
