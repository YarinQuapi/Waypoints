package me.yarinlevi.waypoints.gui;

import me.yarinlevi.waypoints.gui.guilist.MainGui;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiHandler implements Listener {
    public static ItemStack createItem(final Material material, final int amount, final String name, String... lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static void registerGui() {
        new MainGui();
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!isCustomInventory(e.getInventory())) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (isCustomInventory(e.getInventory())) {
            e.setCancelled(true);
        }
    }

    static boolean isCustomInventory(Inventory inv) {
        ArrayList<Inventory> customInventories = new ArrayList<>();
        customInventories.add(MainGui.getMainGui());

        for (Inventory inven : customInventories
        ) {
            if (inv == inven)
                return true;
        }
        return false;
    }
}
