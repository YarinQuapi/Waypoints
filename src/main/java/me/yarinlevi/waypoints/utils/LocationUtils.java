package me.yarinlevi.waypoints.utils;

import org.bukkit.Location;

/**
 * @author YarinQuapi
 */
public class LocationUtils {
    public static LocationData handleLocation(Location loc) {
        return new LocationData(String.valueOf(loc.getBlockX()),
                String.valueOf(loc.getBlockY()),
                String.valueOf(loc.getBlockZ()),
                loc.getWorld().getEnvironment().name(),
                loc.getWorld().getChunkAt(loc).isSlimeChunk());
    }
}
