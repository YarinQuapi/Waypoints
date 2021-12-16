package me.yarinlevi.waypoints.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author YarinQuapi
 */
public class FileUtils {
    public static void registerData(File file, FileConfiguration waypointData) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        } else {
            try {
                waypointData.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                e.printStackTrace();
            }
        }
        try {
            waypointData.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void save(File file, FileConfiguration waypointData) {
        try {
            waypointData.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
