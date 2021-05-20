package me.yarinlevi.waypoints.exceptions;

/**
 * @author YarinQuapi
 */
public class InventoryDoesNotExistException extends Exception {
    public InventoryDoesNotExistException() {
        super("InventoryType was not found!");
    }
}
