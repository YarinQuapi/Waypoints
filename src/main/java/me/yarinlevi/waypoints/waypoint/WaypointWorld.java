package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;

public enum WaypointWorld {
    NORMAL("Overworld"),
    THE_NETHER("The Nether"),
    THE_END("The End");

    @Getter String name;

    WaypointWorld(String name) {
        this.name = name;
    }
}
