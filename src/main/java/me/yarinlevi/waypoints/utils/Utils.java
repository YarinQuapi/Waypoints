package me.yarinlevi.waypoints.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.regex.Pattern;

/**
 * @author YarinQuapi
 */
public class Utils {
    public static final Pattern allowedCharacters = Pattern.compile("([A-z0-9]\\w+)");

    public static String newMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', Constants.PREFIX) + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String newMessageNoPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * 1.16.2 ABOVE ONLY!
     * @param message message
     * @param rgbController An RGB Controller object
     * @return colored string
     */
    public static String newRGBMessage(String message, RGBController rgbController) {
        return (rgbController.toColor() + message);
    }

    /**
     * @param red volume
     * @param green volume
     * @param blue volume
     */
    public record RGBController(int red, int green, int blue) {
        /**
         * Turns given colors to a viable ChatColor
         * @return ChatColor
         */
        public ChatColor toColor() {
            return ChatColor.of(toHex());
        }

        /**
         * @return hex color, e.g. #0xffffff
         */
        public String toHex() {
            Color color = Color.fromRGB(red, green, blue);
            String hex = Integer.toHexString(color.asRGB() & 0xffffff);
            if (hex.length() < 6) {
                hex = "0" + hex;
            }
            hex = "#" + hex;
            return hex;
        }
    }


    public static String repeat(String string, int n) {
        return String.valueOf(string).repeat(Math.max(0, n));
    }


    public static int calculate2DDistance(Vector p1, Vector p2) {
        return calculate2DDistance(p1.getBlockX(), p1.getBlockZ(), p2.getBlockX(), p2.getBlockZ());
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

    private static int calculate2DDistance(int x1, int z1, int x2, int z2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));
    }
}
