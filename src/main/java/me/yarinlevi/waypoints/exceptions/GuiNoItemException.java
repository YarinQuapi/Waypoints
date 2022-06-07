package me.yarinlevi.waypoints.exceptions;

/**
 * @author YarinQuapi
 **/
public class GuiNoItemException extends Exception {
    public GuiNoItemException() {
        this("Sorry, but no items were found in that gui, nothing to render.");
    }

    public GuiNoItemException(String message) {
        super(message);
    }
}
