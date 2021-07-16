package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;

public enum WaypointState {
    PRIVATE("&cPrivate"),
    PUBLIC("&aPublic"),
    SERVER("&bServer");

    @Getter String state;

    WaypointState(String state) {
        this.state = state;
    }
}
