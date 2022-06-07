package me.yarinlevi.waypoints.exceptions;

import me.yarinlevi.waypoints.utils.MessagesUtils;

/**
 * @author YarinQuapi
 */
public class WaypointAlreadyExistsException extends Exception {
    public WaypointAlreadyExistsException() {
        this(MessagesUtils.getMessage("create_failed_exists"));
    }

    public WaypointAlreadyExistsException(String message) {
        super(message);
    }
}
