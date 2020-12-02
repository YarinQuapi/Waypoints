package xyz.yarinlevi.waypoints.data;

import lombok.Getter;
import lombok.Setter;
import xyz.yarinlevi.waypoints.exceptions.HomeAlreadyExistsException;
import xyz.yarinlevi.waypoints.exceptions.HomeDoesNotExistException;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Iterator;

public class Data {
    @Getter @Setter private static File waypointFile;
    @Getter @Setter private static FileConfiguration playerData;

    public static boolean addWaypointToData(Player p, String name, boolean systemInduced) {
        ConfigurationSection playerSection = playerData.getConfigurationSection(p.getUniqueId().toString());
        if(isWaypoint(p, name)) {
            try {
                throw new HomeAlreadyExistsException("HOME_ALREADY_EXISTS");
            } catch (HomeAlreadyExistsException homeAlreadyExists) {
                homeAlreadyExists.printStackTrace();
            }
            return false;
        } else {
            ConfigurationSection section = playerSection.createSection(name);
            section.set("item", "DIRT");
            section.set("location", p.getLocation());
            section.set("systemInduced", systemInduced);
            saveData();
            return true;
        }
    }

    public static boolean deleteWaypoint(Player p, String name) {
        ConfigurationSection playerSection = playerData.getConfigurationSection(p.getUniqueId().toString());
        if(!isWaypoint(p, name)) {
            try {
                throw new HomeDoesNotExistException("HOME_DOES_NOT_EXIST");
            } catch (HomeDoesNotExistException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            playerSection.set(name, null);
            saveData();
            return true;
        }
    }

    public static ConfigurationSection getWaypointData(Player p, String name) {
        return isWaypoint(p, name) ? playerData.getConfigurationSection(p.getUniqueId().toString()).getConfigurationSection(name) : null;
    }

    public static boolean isWaypoint(Player p, String name) {
        return playerData.getConfigurationSection(p.getUniqueId().toString()).contains(name);
    }

    public static boolean deleteAllSystemInducedWaypoints(Player p) {
        ConfigurationSection playerSection = playerData.getConfigurationSection(p.getUniqueId().toString());
        Iterator<String> iterator = playerData.getConfigurationSection(p.getUniqueId().toString()).getKeys(false).iterator();
        if(!iterator.hasNext())
            return false;
        while (iterator.hasNext()) {
            ConfigurationSection waypoint = getWaypointData(p, iterator.next());
            if(waypoint.getBoolean("systemInduced")) {
                playerSection.set(waypoint.getName(), null);
            }
        }
        return true;
    }

    public static void saveData() {
        try {
            playerData.save(waypointFile);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
