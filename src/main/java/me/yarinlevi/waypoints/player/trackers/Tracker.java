package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Tracker {
    Map<Player, Waypoint> trackedPlayers = new HashMap<>();

    protected abstract void update();

    public abstract ETracker getETracker();

    boolean isTracked(Player player) {
        return trackedPlayers.containsKey(player);
    }

    boolean track(Player player, Waypoint waypoint) {
        if (trackedPlayers.containsKey(player)) {
            return false;
        } else {
            trackedPlayers.put(player, waypoint);
            return true;
        }
    }

    boolean unTrack(Player player) {
        if (trackedPlayers.containsKey(player)) {
            trackedPlayers.remove(player);
            return true;
        } else return false;
    }
}
