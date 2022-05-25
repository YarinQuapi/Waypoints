package me.yarinlevi.waypoints.data;

import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;

import java.util.List;
import java.util.UUID;

public interface IData {
    void closeDatabase();

    List<Waypoint> getPublicWaypoints();

    void renameWaypoint(UUID uuid, String waypoint, String name) throws WaypointDoesNotExistException;

    void addWaypoint(UUID uuid, Waypoint waypoint);

    void removeWaypoint(UUID uuid, String waypoint);

    Waypoint getWaypoint(UUID uuid, String waypoint);

    void updateWaypointItem(UUID uuid, String waypoint, String item);

    boolean isWaypoint(UUID uuid, String waypoint);

    void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws WaypointDoesNotExistException;

    void loadPlayer(UUID uuid);

    void unloadPlayer(UUID uuid);
}
