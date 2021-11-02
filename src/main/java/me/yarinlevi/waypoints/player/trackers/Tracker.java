package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public interface Tracker {
    Map<Player, Location> trackedPlayers = new HashMap<>();


    void update();

    ETracker getETracker();

    default boolean isTracked(Player player) {
        return trackedPlayers.containsKey(player);
    }

    default boolean track(Player player, Waypoint waypoint) {
        if (trackedPlayers.containsKey(player)) {
            return false;
        } else {
            trackedPlayers.put(player, waypoint.getLocation());
            return true;
        }
    }

    default boolean unTrack(Player player) {
        if (trackedPlayers.containsKey(player)) {
            trackedPlayers.remove(player);
            return true;
        } else return false;
    }
}
