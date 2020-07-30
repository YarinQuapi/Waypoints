package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    public static String newMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', Waypoints.getInstance().getPrefix()) + ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String newMessageNoPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
