package me.yarinlevi.waypoints.exceptions;

import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.types.StateIdentifier;

/**
 * @author YarinQuapi
 **/
public class WaypointLimitReachedException extends Exception {
    public WaypointLimitReachedException() {
        this(StateIdentifier.ALL);
    }

    public WaypointLimitReachedException(StateIdentifier state) {
        this(MessagesUtils.getMessage("create_failed_limit", state.getState()));
    }

    public WaypointLimitReachedException(String message) {
        super(message);
    }
}
