package xyz.yarinlevi.waypoints.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import xyz.yarinlevi.waypoints.gui.helpers.Gui;
import xyz.yarinlevi.waypoints.gui.inventories.MainGui;

public class GuiHandler implements Listener {
    public static void registerGui() {
        new MainGui().initialize().register();
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

    public static boolean isCustomInventory(Inventory inv) {
        for (Inventory inven : Gui.guiList.values()) {
            if (inv.equals(inven))
                return true;
        }
        return false;
    }
}
