package xyz.yarinlevi.waypoints.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.gui.helpers.Gui;
import xyz.yarinlevi.waypoints.gui.inventories.ListWaypointsGui;
import xyz.yarinlevi.waypoints.gui.inventories.MainGui;

public class GuiHandler implements Listener {
    public static void registerGui() {
        MainGui mainGui = new MainGui();
        mainGui.initialize().register();
        Bukkit.getPluginManager().registerEvents(mainGui, Waypoints.getInstance());

        ListWaypointsGui listWaypointsGui = new ListWaypointsGui();
        listWaypointsGui.load();
        Bukkit.getPluginManager().registerEvents(listWaypointsGui, Waypoints.getInstance());
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
