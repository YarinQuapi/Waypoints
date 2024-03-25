package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Tracker {
    Map<Player, Waypoint> trackedPlayers = new HashMap<>();

    private boolean change_tracker_while_tracking = Waypoints.getInstance().getConfig().getBoolean("gui.change_tracker_while_tracking", true);

    protected abstract void update();

    public abstract ETracker getETracker();

    boolean isTracked(Player player) {
        return trackedPlayers.containsKey(player);
    }

    boolean track(Player player, Waypoint waypoint) {
        if (trackedPlayers.containsKey(player)) {
            if (change_tracker_while_tracking) {
                trackedPlayers.remove(player);
                trackedPlayers.put(player, waypoint);
                return true;
            }
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
