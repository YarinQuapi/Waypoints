package me.yarinlevi.waypoints.data;

import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;

import java.util.List;
import java.util.UUID;

public interface IData {
    List<Waypoint> getPublicWaypoints();

    void renamePublicWaypoint(UUID uuid, String waypoint, String name) throws PlayerDoesNotExistException;

    void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws PlayerDoesNotExistException, WaypointDoesNotExistException;

    void loadPlayer(UUID uuid);

    void unloadPlayer(UUID uuid);

    void savePlayer(UUID uuid);

    void saveFile();
}
