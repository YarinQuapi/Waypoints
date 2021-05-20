package me.yarinlevi.waypoints.player;

import lombok.Getter;
import me.yarinlevi.waypoints.waypoint.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class PlayerData {
    @Getter private final List<Waypoint> waypointList = new ArrayList<>();

    public PlayerData(List<Waypoint> waypoints) {
        this.waypointList.addAll(waypoints);
    }
}
