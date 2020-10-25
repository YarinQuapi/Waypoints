package xyz.yarinlevi.waypoints.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
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
        ChatColor color = ChatColor.of(getHexFromRGB(red, green, blue));
        return (color + message);
    }

    private static String getHexFromRGB(int red, int green, int blue) {
        org.bukkit.Color color = Color.fromRGB(red, green, blue);
        String hex = Integer.toHexString(color.asRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        return hex;
    }
}
