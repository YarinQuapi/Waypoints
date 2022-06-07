package me.yarinlevi.waypoints.exceptions;

import me.yarinlevi.waypoints.utils.MessagesUtils;

/**
 * @author YarinQuapi
 */
public class WaypointDoesNotExistException extends Exception {
    public WaypointDoesNotExistException() {
        this(MessagesUtils.getMessage("action_failed_not_found"));
    };
    public WaypointDoesNotExistException(String message) {
        super(message);
    }
}
