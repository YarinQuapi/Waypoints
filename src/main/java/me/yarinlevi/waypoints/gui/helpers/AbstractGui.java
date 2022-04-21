package me.yarinlevi.waypoints.gui.helpers;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.gui.helpers.types.Page;
import me.yarinlevi.waypoints.utils.MessagesUtils;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public abstract class AbstractGui implements Listener, IGui {
    @Getter private Inventory inventory;
    @Getter @Setter private InventoryType inventoryType = InventoryType.CHEST;
    @Getter @Setter private String title, key;
    @Getter @Setter private int slots;
    @Getter private int maxPages;
    @Getter private int currentPage = 1;
    @Getter private final Map<Integer, Page> pages = new HashMap<>();
    @Getter private final HashMap<Integer, ItemStack> items = new HashMap<>();
    private final int[] lockedSlots = new int[]{ slots-1, slots-2, slots-3, slots-4, slots-5, slots-6, slots-7, slots-8, slots-9 };

    public abstract void run(Player player);

    public Inventory initializeInventory() throws InventoryDoesNotExistException {
        if (inventoryType.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, slots, title);

            this.initializeSlots();

            return inventory;
        } else if (inventoryType.equals(InventoryType.ANVIL)) {
            return Bukkit.createInventory(null, InventoryType.ANVIL, title);
        } else {
            throw new InventoryDoesNotExistException();
        }
    }

    public void openPage(Player player, int page) {
        this.pages.get(page).constructPage(player, inventory);
    }

    public void nextPage(Player player) {
        if (currentPage < maxPages) {
            currentPage++;

            this.openPage(player, currentPage);
        } else {
            player.closeInventory();
            player.sendMessage(MessagesUtils.getMessageFromData("gui.last-page"));
        }
    }

    public void previousPage(Player player) {
        if (currentPage <= maxPages) {
            currentPage--;

            this.openPage(player, currentPage);
        }
    }

    public void initializeSlots() {
        int size = items.keySet().size() + (lockedSlots.length * (items.keySet().size() / slots));

        this.maxPages = size / slots;
        boolean leftOver = size % slots > 0;

        if (leftOver || this.maxPages == 0) {
            this.maxPages++;
        }

        Page page = new Page(1, slots, lockedSlots, null, this.maxPages != 1);

        Bukkit.broadcastMessage("Total Size (including lockedSlots): " + size);
        Bukkit.broadcastMessage("Max Pages: " + this.maxPages);
        Bukkit.broadcastMessage("Max items: " + items.size());

        int j = 0;
        for (int i = 0; i < size; i++) {

            if (j < (slots - lockedSlots.length)) {
                page.addItem(j, items.get(i));
                j++;

                if (i == size-1) {
                    pages.put(page.getId(), page);
                }
            } else {
                j = 0;
                pages.put(page.getId(), page);

                Bukkit.broadcastMessage(page.getId() + "#, Items in page: " + j);

                page = new Page(page.getId() + 1, slots, lockedSlots, null, this.maxPages != page.getId() + 1);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() == inventory) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
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
