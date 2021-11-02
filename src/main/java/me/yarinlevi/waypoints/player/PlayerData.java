package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class PlayerData {
    @Getter private final List<Waypoint> waypointList = new ArrayList<>();
    @Getter @Setter private boolean playerDeathPoints = true;
    @Getter @Setter private ETracker eTracker = ETracker.ActionBar;

    public PlayerData(List<Waypoint> waypoints) {
        this.waypointList.addAll(waypoints);
    }
}
