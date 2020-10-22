package xyz.yarinlevi.waypoints.utils;

import org.bukkit.Location;

import java.util.HashMap;

public class LocationHandler {
    public static HashMap<String, String> handleLocation(Location loc) {
        HashMap<String, String> locDetail = new HashMap<>();
        locDetail.put("x", String.valueOf(loc.getBlockX()));
        locDetail.put("y", String.valueOf(loc.getBlockY()));
        locDetail.put("z", String.valueOf(loc.getBlockZ()));
        locDetail.put("world", loc.getWorld().getEnvironment().name());
        locDetail.put("isSlimeChunk", String.valueOf(loc.getWorld().getChunkAt(loc.getBlock()).isSlimeChunk()));
        return locDetail;
    }
}
