package me.yarinlevi.waypoints.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

    public int distance(Player player) {
        return Utils.calculateDistance(player.getLocation().toVector(), this.getLocation().toVector());
    }

    public boolean isSlimeChunk() {
        return this.getLocation().getChunk().isSlimeChunk();
    }
}