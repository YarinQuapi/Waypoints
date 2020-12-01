package xyz.yarinlevi.waypoints.exceptions;

public class InventoryDoesNotExistException extends Exception {
    public InventoryDoesNotExistException() {
        super("InventoryType was not found!");
    }
}
