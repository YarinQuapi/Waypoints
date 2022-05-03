package me.yarinlevi.waypoints.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author YarinQuapi
 */
public record LocationData(String x, String y, String z,
                           String world) {
    public Location getLocation() {
        return new Location(
                Bukkit.getWorld(world),
                Double.parseDouble(x),
                Double.parseDouble(y),
                Double.parseDouble(z));
    }

    public boolean isSlimeChunk() {
        return this.getLocation().getChunk().isSlimeChunk();
    }
}