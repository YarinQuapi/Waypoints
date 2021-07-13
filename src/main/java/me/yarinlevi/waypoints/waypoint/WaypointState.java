package me.yarinlevi.waypoints.waypoint;

public enum WaypointState {
    PRIVATE("&cPrivate"),
    PUBLIC("&aPublic"),
    SERVER("&bServer");

    String state;

    WaypointState(String state) {
        this.state = state;
    }
}
