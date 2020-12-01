package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.LocationHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class WaypointListGui extends IGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypointlist");
        this.setSlots(9*3);
        this.setTitle("Waypoint list");

        ItemStack itemStack = new ItemStack(Material.DIRT);
        int i = 0;
        for (String waypointName : WaypointManager.tabCompleterList(player)) {
            ItemMeta itemMeta = itemStack.getItemMeta();

            ConfigurationSection wp = Data.getWaypointData(player, waypointName);

            Location loc = (Location) wp.get("location");
            HashMap<String, String> locDetail = LocationHandler.handleLocation(loc);

            ArrayList<String> lore = new ArrayList<>();
            lore.add("Coordinates: " + loc.toVector());

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(waypointName);

            itemStack.setItemMeta(itemMeta);

            this.getItems().put(i, itemStack);
            i++;
        }

        player.openInventory(this.initializeInventory());
        player.sendMessage("opened a gui for you.");
    }
}
