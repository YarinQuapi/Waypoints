package me.yarinlevi.waypoints.api;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author YarinQuapi
 * @since 3.0.0-Beta-1.4
 */
public final class QWaypointsAPI {
    /**
     * Gets the nearest waypoint to a player
     * @param player The player
     * @return The nearest waypoint, null if none
     */
    @Nullable
    public static Waypoint getNearestWaypoint(Player player) {
        return Waypoints.getInstance().getWaypointHandler().getNearestWaypoint(player);
    }

    /**
     * Gets all of the player's waypoints
     * @param player The player
     * @return A list with all of his waypoints
     */
    public static List<Waypoint> getPlayerWaypoints(OfflinePlayer player) {
        return Waypoints.getInstance().getWaypointHandler().getWaypoints(player);
    }

    /**
     * Gets a waypoint from a player
     * @param player The player
     * @param waypoint The name of the waypoint
     * @return The waypoint requested, null if not found
     */
    @Nullable
    public static Waypoint getPlayerWaypoint(OfflinePlayer player, String waypoint) {
        return Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypoint);
    }

    /**
     * Adds a waypoint to a player
     * @param waypoint The waypoint you would like to add (Player should be assigned in waypoint object)
     * @return True if was added
     * @throws PlayerNotLoadedException The player is offline or bugged, a re-log should fix
     * @throws WaypointAlreadyExistsException The player already has a waypoint with that name
     */
    public static boolean addNewWaypoint(Waypoint waypoint) throws PlayerNotLoadedException, WaypointAlreadyExistsException {
        return Waypoints.getInstance().getWaypointHandler().addWaypoint(waypoint.getOwner(), waypoint);
    }

    /**
     * Removes a waypoint from a player
     * @param player The player
     * @param waypoint The name of the waypoint you want to remove
     * @return True if was removed
     * @throws PlayerNotLoadedException The player is unloaded or doesn't exist. "/wpa load [playerName]" should fix the problem, a re-log works too.
     * @throws WaypointDoesNotExistException The player already has a waypoint with that name
     */
    public static boolean removeWaypoint(OfflinePlayer player, String waypoint) throws PlayerNotLoadedException, WaypointDoesNotExistException {
        return Waypoints.getInstance().getWaypointHandler().removeWaypoint(player, waypoint);
    }
}
