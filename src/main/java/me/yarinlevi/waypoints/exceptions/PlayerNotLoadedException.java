package me.yarinlevi.waypoints.exceptions;

import me.yarinlevi.waypoints.utils.MessagesUtils;

/**
 * @author YarinQuapi
 */
public class PlayerNotLoadedException extends Exception {
    public PlayerNotLoadedException() {
        this(MessagesUtils.getMessage("offline_player"));
    }

    public PlayerNotLoadedException(String message) {
        super(message);
    }
}
