package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;

/**
 * @author YarinQuapi
 */
public class Constants {
    public static boolean DEATH_POINTS;
    public static String PREFIX;
    public static boolean PUBLIC_WAYPOINTS;

    public Constants() {
        DEATH_POINTS = Waypoints.getInstance().getConfig().getBoolean("DeathPoints");
        PUBLIC_WAYPOINTS = Waypoints.getInstance().getConfig().getBoolean("PublicWaypoints");
        PREFIX = Waypoints.getInstance().getConfig().getString("Prefix").replaceAll("&", "§");
    }
}
