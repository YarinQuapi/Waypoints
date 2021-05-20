package me.yarinlevi.waypoints.gui.helpers;

import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author YarinQuapi
 */
public interface IGui {
    void run(Player player);

    Inventory initializeInventory() throws InventoryDoesNotExistException;
}
