package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;

/**
 * @author YarinQuapi
 */
public enum WaypointWorld {
    NORMAL("Overworld"),
    NETHER("The Nether"),
    THE_END("The End");

    @Getter String name;

    WaypointWorld(String name) {
        this.name = name;
    }
}
