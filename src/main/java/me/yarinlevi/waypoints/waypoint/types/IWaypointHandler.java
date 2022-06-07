package me.yarinlevi.waypoints.waypoint.types;

import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public interface IWaypointHandler {
    void addPlayer(OfflinePlayer player, PlayerData data);

    void removePlayer(Player player);

    PlayerData getPlayer(OfflinePlayer offlinePlayer);

    List<Waypoint> getWaypoints(OfflinePlayer player);

    List<Waypoint> getWaypoints(OfflinePlayer player, WaypointWorld world);


    Waypoint getNearestWaypoint(OfflinePlayer player);

    List<Waypoint> getPublicWaypoints();

    List<Waypoint> getDeathPoints(OfflinePlayer player);

    Waypoint getOfflineWaypoint(OfflinePlayer player, String waypoint);

    Waypoint getWaypoint(OfflinePlayer player, String waypoint);

    void addWaypoint(OfflinePlayer player, Waypoint waypoint) throws PlayerNotLoadedException, WaypointAlreadyExistsException, WaypointLimitReachedException;

    void removeWaypoint(OfflinePlayer player, String waypoint) throws PlayerNotLoadedException, WaypointDoesNotExistException;

    void removeWaypoint(OfflinePlayer player, Waypoint waypoint) throws PlayerNotLoadedException;
}
