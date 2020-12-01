package xyz.yarinlevi.waypoints.gui.helpers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Gui implements Listener {
    @Getter public static final HashMap<String, Inventory> guiList = new HashMap<>();


    @Getter private Inventory inventory;
    @Getter @Setter private String title, key;
    @Getter @Setter private int slots;
    @Getter private final HashMap<Integer, ItemStack> items = new HashMap<>();


    public static Inventory getGui(String key) {
        return guiList.get(key);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void register() {
        inventory = Bukkit.createInventory(null, slots, title);
        initializeItems();
        guiList.put(key, inventory);
    }

    public void initializeItems() {
        for (int slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot));
        }
    }
}
