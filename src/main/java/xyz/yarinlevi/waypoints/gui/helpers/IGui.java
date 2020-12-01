package xyz.yarinlevi.waypoints.gui.helpers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;

import java.util.HashMap;

public abstract class IGui implements Listener {
    @Getter private Inventory inventory;
    @Getter @Setter private InventoryType inventoryType = InventoryType.CHEST;
    @Getter @Setter private String title, key;
    @Getter @Setter private int slots;
    @Getter private final HashMap<Integer, ItemStack> items = new HashMap<>();

    public abstract void run(Player player);

    public Inventory initializeInventory() throws InventoryDoesNotExistException {
        if (inventoryType.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, slots, title);

            for (int slot : items.keySet()) {
                inventory.setItem(slot, items.get(slot));
            }
            return inventory;
        } else if (inventoryType.equals(InventoryType.ANVIL)) {
            return Bukkit.createInventory(null, InventoryType.ANVIL, title);
        } else {
            throw new InventoryDoesNotExistException();
        }
    }

    public void initializeSlots(Inventory inventory) {
        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryMoveItem(final InventoryMoveItemEvent e) {
        if (e.getInitiator() == inventory) {
            e.setCancelled(true);
        }
    }
}
