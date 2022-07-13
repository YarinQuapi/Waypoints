package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author YarinQuapi
 */
public class Constants {
    public static String PREFIX;
    public static boolean DEATH_POINTS;
    public static boolean PUBLIC_WAYPOINTS;
    public static boolean WAYPOINT_TELEPORTING;
    public static boolean ECONOMY_SUPPORT;
    public static int WAYPOINT_TELEPORT_COST;

    public Constants() {
        FileConfiguration config = Waypoints.getInstance().getConfig();

        PREFIX = config.getString("Prefix").replaceAll("&", "§");
        DEATH_POINTS = config.getBoolean("DeathPoints");
        PUBLIC_WAYPOINTS = config.getBoolean("PublicWaypoints");
        WAYPOINT_TELEPORTING = config.getBoolean("WaypointTeleporting.enabled", false);
        ECONOMY_SUPPORT = config.getBoolean("WaypointTeleporting.economy", false);
        WAYPOINT_TELEPORT_COST = config.getInt("WaypointTeleporting.cost", 20);
    }
}
