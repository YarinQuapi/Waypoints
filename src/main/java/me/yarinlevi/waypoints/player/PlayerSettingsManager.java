package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.helpers.FileUtils;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author YarinQuapi
 */
public class PlayerSettingsManager {
    @Getter @Setter private File settingsFile;
    @Getter @Setter private FileConfiguration settingsData;

    @Getter private final Map<String, Map<String, Integer>> waypointLimits = new HashMap<>();

    public PlayerSettingsManager() {
        settingsFile = new File(Waypoints.getInstance().getDataFolder(), "playerSettings.yml");
        settingsData = YamlConfiguration.loadConfiguration(settingsFile);

        FileUtils.registerData(settingsFile, settingsData);
        FileUtils.save(settingsFile, settingsData);
    }

    public void loadPlayerSettings(UUID uuid, PlayerData data) {
        if (settingsData.contains(uuid.toString())) {
            data.setPlayerDeathPoints(settingsData.getBoolean(uuid + ".deathpoints", true));
            data.setETracker(Waypoints.getInstance().getTrackerManager().getTracker(settingsData.getString(uuid + ".etracker", ETracker.ActionBar.getKey())).getETracker());
        }
    }

    public void unloadPlayerSettings(UUID uuid, PlayerData data) {
        settingsData.set(uuid + ".deathpoints", data.isPlayerDeathPoints());
        settingsData.set(uuid + ".etracker", data.getETracker().getKey());

        FileUtils.save(settingsFile, settingsData);
    }
}
