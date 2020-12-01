package xyz.yarinlevi.waypoints.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import xyz.yarinlevi.waypoints.Waypoints;

public class Utils {
    public static String newMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()) + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String newMessageNoPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 1.16.2 ABOVE ONLY!
     * @param message message
     * @param red red volume
     * @param green green volume
     * @param blue blue volume
     * @return colored string
     */
    public static String newRGBMessage(String message, int red, int green, int blue) {
        return (ChatColor.of(getHexFromRGB(red,green,blue)) + message);
    }

    public static String getHexFromRGB(int r, int g, int b) {
        Color color = Color.fromRGB(r,g,b);
        String hex = Integer.toHexString(color.asRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        return hex;
    }

    public static int calculateDistance(Location p1, Location p2) {
        return (int) Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2.0) + Math.pow(p1.getY()-p2.getY(), 2) + Math.pow(p1.getZ() - p2.getZ(), 2));
    }
}
