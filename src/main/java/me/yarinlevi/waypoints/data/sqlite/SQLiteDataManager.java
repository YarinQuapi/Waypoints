package me.yarinlevi.waypoints.data.sqlite;

import me.yarinlevi.waypoints.data.IData;
import me.yarinlevi.waypoints.exceptions.PlayerDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointState;

import java.util.List;
import java.util.UUID;

/**
 * @author YarinQuapi
 **/
public class SQLiteDataManager implements IData {
    public SQLiteDataManager() {

    }

    @Override
    public List<Waypoint> getPublicWaypoints() {
        return null;
    }

    @Override
    public void renamePublicWaypoint(UUID uuid, String waypoint, String name) throws PlayerDoesNotExistException {

    }

    @Override
    public void setWaypointState(UUID uuid, String waypoint, WaypointState state) throws PlayerDoesNotExistException, WaypointDoesNotExistException {

    }

    @Override
    public void loadPlayer(UUID uuid) {

    }

    @Override
    public void unloadPlayer(UUID uuid) {

    }

    @Override
    public void savePlayer(UUID uuid) {

    }

    @Override
    public void saveFile() {

    }
}
