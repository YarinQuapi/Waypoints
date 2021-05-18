package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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


    public static int calculateDistance(Vector p1, Vector p2) {
        return calculateDistance(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ(), p2.getBlockX(), p2.getBlockY(), p2.getBlockZ());
    }

    public static int calculateDistance(Location p1, Location p2) {
        return calculateDistance(p1.getBlockX(), p1.getBlockY(), p1.getBlockZ(), p2.getBlockX(), p2.getBlockY(), p2.getBlockZ());
    }
    
    private static int calculateDistance(int x1, int y1, int z1, int x2, int y2, int z2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
    }
}
