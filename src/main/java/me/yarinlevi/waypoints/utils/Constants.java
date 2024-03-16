package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author YarinQuapi
 */
public class Constants {
    public static String PREFIX;

    public static String PLUGIN_NAME = "§b§lQWaypoints";
    public static String PLUGIN_VERSION = "§e§l5.0";

    public static boolean DEATH_POINTS;
    public static boolean PUBLIC_WAYPOINTS;
    public static boolean WAYPOINT_TELEPORTING;
    public static boolean TELEPROT_ECONOMY_SUPPORT;
    public static int WAYPOINT_TELEPORT_COST;

    public Constants() {
        FileConfiguration config = Waypoints.getInstance().getConfig();

        PREFIX = config.getString("prefix").replaceAll("&", "§");
        DEATH_POINTS = config.getBoolean("death_points");
        PUBLIC_WAYPOINTS = config.getBoolean("public_waypoints");
        WAYPOINT_TELEPORTING = config.getBoolean("waypoint_teleporting.enabled", false);
        TELEPROT_ECONOMY_SUPPORT = config.getBoolean("waypoint_teleporting.economy", false);
        WAYPOINT_TELEPORT_COST = config.getInt("waypoint_teleporting.cost", 20);
    }
}
