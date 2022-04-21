package me.yarinlevi.waypoints.gui.helpers.types;

import lombok.Getter;
import me.yarinlevi.waypoints.gui.global.items.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author YarinQuapi
 **/
public class Page {
    @Getter private final int id;
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final int slots;
    private final int[] lockedSlots;
    private final boolean hasNext;

    /**
     * @param id The page, MUST be a positive number, if not, it will be set to 1
     * @param slots The maximum slots
     * @param lockedSlots The locked slots
     * @param items The items
     */
    public Page(int id, int slots, int[] lockedSlots, Map<Integer, ItemStack> items, boolean hasNext) {
        this.id = id <= 0 ? 1 : id;

        if (items != null) {
            this.items.putAll(items);
        }

        this.slots = slots;
        this.lockedSlots = lockedSlots;
        this.hasNext = hasNext;
    }

    /**
     * Construct and views the page for the player
     * @param player the player to view the page for
     * @param inventory the inventory to view the page in (Must be created before with @see {@link me.yarinlevi.waypoints.gui.helpers.AbstractGui})
     */
    public void constructPage(Player player, Inventory inventory) {
        player.closeInventory();
        inventory.clear();

        Bukkit.broadcastMessage("1");

        Bukkit.broadcastMessage("Items: " + items.size());

        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(i, items.get(i));
            Bukkit.broadcastMessage("Item added to slot " + i);
        }

        inventory.setItem(slots - (lockedSlots.length - Items.ITEM_MENU_SLOT), Items.ITEM_MENU);

        if (this.id > 1) {
            inventory.setItem(slots - (lockedSlots.length - Items.ITEM_PREVIOUS_SLOT), Items.ITEM_PREVIOUS);
        }

        if (hasNext) {
            inventory.setItem(slots - (lockedSlots.length - Items.ITEM_NEXT_SLOT), Items.ITEM_NEXT);
        }

        player.openInventory(inventory);
    }

    /**
     * Adds a new item to the page
     * @param slot the slot to add the item to
     * @param item the item to add
     * @return true if the item was added, false if the slot is locked or already occupied @see {@link #isSlotLocked(int)}, {@link #isSlotOccupied(int)} and @see {@link #replaceItem(int, ItemStack)}
     */
    public boolean addItem(int slot, ItemStack item) {
        if (items.containsKey(slot)) return false;
        items.put(slot, item);
        return true;
    }

    /**
     * Checks if the slot is locked
     * @param slot the slot to check
     * @return true if the slot is locked, false if not
     */
    public boolean isSlotLocked(int slot) {
        for (int lockedSlot : lockedSlots) {
            if (lockedSlot == slot) return true;
        }
        return false;
    }

    /**
     * Checks if there is an item in the slot
     * @param slot the slot to check
     * @return true if there is an item in the slot, false if not
     */
    public boolean isSlotOccupied(int slot) {
        return items.containsKey(slot);
    }

    /**
     * Replaces an item in the page
     * @param slot the slot to replace the item in
     * @param item the item to replace with
     * @return the old item if found, if not then null
     */
    @Nullable
    public ItemStack replaceItem(int slot, ItemStack item) {
        return items.put(slot, item);
    }
}
