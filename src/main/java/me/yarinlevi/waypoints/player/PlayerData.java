package me.yarinlevi.waypoints.player;

import lombok.Getter;
import me.yarinlevi.waypoints.waypoint.Waypoint;

import java.util.*;
import java.util.ArrayList;

public class PlayerData {
    @Getter private final List<Waypoint> waypointList = new ArrayList<>();
}
