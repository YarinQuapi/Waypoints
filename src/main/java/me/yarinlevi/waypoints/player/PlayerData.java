package me.yarinlevi.waypoints.player;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class PlayerData {
    @Getter private final List<Waypoint> waypointList = new ArrayList<>();
    @Getter @Setter private boolean playerDeathPoints = true;
    @Getter @Setter private int totalWaypointsLimit = 10;
    @Getter @Setter private int netherWaypointsLimit = 5;
    @Getter @Setter private int endWaypointsLimit = 5;
    @Getter @Setter private ETracker eTracker = ETracker.ActionBar;

    public PlayerData(List<Waypoint> waypoints) {
        this.waypointList.addAll(waypoints);
    }

    public PlayerData(Player player, List<Waypoint> waypoints) {
        this.waypointList.addAll(waypoints);


    }
}
