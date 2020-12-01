package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.yarinlevi.waypoints.gui.helpers.Gui;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.ArrayList;

public class MainGui extends Gui {
    public Gui initialize() {
        this.setKey("WAYPOINTS_MAIN_GUI");
        this.setSlots(9);
        this.setTitle(Utils.newRGBMessage("QWaypoints", 130, 179, 171));

        //Item Initialization
        ItemStack createWaypointButton = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = createWaypointButton.getItemMeta();

        assert meta != null;
        meta.setDisplayName(Utils.newRGBMessage("Create new Waypoint", 35, 245, 24));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.newRGBMessage("Click here to create a new Waypoint!", 30, 219, 20));

        meta.setLore(lore);

        createWaypointButton.setItemMeta(meta);


        this.getItems().put(2, createWaypointButton);
        return this;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        player.sendMessage("surprisingly this is working correctly.");
    }
}
