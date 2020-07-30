package me.yarinlevi.waypoints.gui.guilist;

import lombok.Getter;
import me.yarinlevi.waypoints.gui.GuiHandler;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class MainGui {
    @Getter private static Inventory mainGui;

    public MainGui() {
        mainGui = Bukkit.createInventory(null, 27, Utils.newMessageNoPrefix("&b&lQWaypoints"));

        initializeItems();
    }

    private static void initializeItems() {
        mainGui.setItem(10, GuiHandler.createItem(Material.GREEN_WOOL, 1, Utils.newMessageNoPrefix("&a&lCreate new Waypoint")));
        mainGui.setItem(13, GuiHandler.createItem(Material.ORANGE_WOOL, 1, Utils.newMessageNoPrefix("&6&lList Waypoints")));
        mainGui.setItem(16, GuiHandler.createItem(Material.RED_WOOL, 1, Utils.newMessageNoPrefix("&c&lDelete Waypoint")));
    }
}
