package xyz.yarinlevi.waypoints.gui.helpers;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.gui.GuiHandler;

import java.util.HashMap;

public class Gui implements Listener {
    @Getter public static final HashMap<String, Inventory> guiList = new HashMap<>();

    @Getter @Setter private Inventory inventory;
    @Getter @Setter private InventoryType inventoryType;
    @Getter @Setter private String title, key;
    @Nullable @Getter @Setter private int slots;
    @Getter private final HashMap<Integer, ItemStack> items = new HashMap<>();

    public static Inventory getGui(String key) {
        return guiList.get(key);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void register() {
        if (inventoryType.equals(InventoryType.CHEST)) {
            inventory = Bukkit.createInventory(null, slots, title);
            initializeItems();
        } else if (inventoryType.equals(InventoryType.ANVIL)) {
            inventory = Bukkit.createInventory(null, InventoryType.ANVIL, title);
        }
        guiList.put(key, inventory);
    }

    public void initializeItems() {
        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!GuiHandler.isCustomInventory(e.getInventory())) return;
        e.setCancelled(true);
    }
}
