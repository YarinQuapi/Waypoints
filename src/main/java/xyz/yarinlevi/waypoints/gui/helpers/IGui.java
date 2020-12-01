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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class IGui implements Listener {
    @Getter private Inventory inventory;
    @Getter @Setter private String title, key;
    @Getter @Setter private int slots;
    @Getter private final HashMap<Integer, ItemStack> items = new HashMap<>();

    public abstract void run(Player player);

    public Inventory initializeInventory() {
        inventory = Bukkit.createInventory(null, slots, title);

        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
        return inventory;
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
