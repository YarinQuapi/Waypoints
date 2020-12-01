package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.ArrayList;

public class WaypointListGui extends IGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypointlist");
        this.setSlots(9*3);
        this.setTitle("Waypoint list");

        int i = 0;
        for (String waypointName : WaypointManager.tabCompleterList(player)) {
            ItemStack itemStack = new ItemStack(Material.DIRT);
            ItemMeta itemMeta = itemStack.getItemMeta();

            ConfigurationSection wp = Data.getWaypointData(player, waypointName);

            Location loc = (Location) wp.get("location");
            Vector vec = loc.toVector();

            ArrayList<String> lore = new ArrayList<>();
            String coordinatesString = String.format(Utils.newMessageNoPrefix("&eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s"), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            lore.add(coordinatesString);

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Utils.newMessageNoPrefix("&d" + waypointName));

            itemStack.setItemMeta(itemMeta);

            this.getItems().put(i, itemStack);
            i++;
        }

        try {
            player.openInventory(this.initializeInventory());
        } catch (InventoryDoesNotExistException e) {
            e.printStackTrace();
        }
    }
}
