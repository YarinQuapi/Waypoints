package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.gui.helpers.Gui;
import xyz.yarinlevi.waypoints.utils.LocationHandler;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

public class ListWaypointsGui extends Gui implements Listener {
    private final HashMap<Integer, Integer> slotTranslation = new HashMap<>();

    public void load() {
        slotTranslation.put(1, 8);
        slotTranslation.put(2, 17);
        slotTranslation.put(3, 26);
        slotTranslation.put(4, 35);
        slotTranslation.put(5, 44);
        slotTranslation.put(6, 53);
    }

    public Gui initialize(Player player) {
        Set<String> rawWaypointNames = Data.getRawWaypointNames(player);

        this.setKey("WAYPOINTS_PERSONAL");
        this.setInventoryType(InventoryType.CHEST);

        this.getItems().put(4, new ItemStack(Material.NETHER_STAR));
        this.setSlots(45);
        this.setTitle("Waypoints for: " + player.getName());

        if(rawWaypointNames != null && !rawWaypointNames.isEmpty()) {
            int slotsRequired = initializeSlots(rawWaypointNames.size());
            /*
            if(slotsRequired > 27) {
                //TODO: Handle multiple pages
            } else {
                this.setSlots(45);
            }
             */

            ConfigurationSection wp;
            Location loc;
            HashMap<String, String> locDetail;
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            int i = 9;
            for (String rawWaypointName : rawWaypointNames) {
                wp = Data.getWaypointData(player, rawWaypointName);

                loc = (Location) wp.get("location");
                locDetail = LocationHandler.handleLocation(loc);
                ItemMeta meta = item.getItemMeta();

                ArrayList<String> lore = new ArrayList<>();
                Stream.of(
                        Utils.newRGBMessage(String.format("Coordinates: %s %s %s", locDetail.get("x"), locDetail.get("y"), locDetail.get("z")), 152, 15, 156)
                ).forEach(lore::add);

                meta.setLore(lore);
                meta.setDisplayName(rawWaypointName);
                item.setItemMeta(meta);
                this.getItems().put(i, item);
                i = i + 1;
            }
            return this;
        }
        return this;
    }

    private int initializeSlots(int numOfWaypoints) {
        for (int key : slotTranslation.keySet()) {
            if (slotTranslation.get(key) <= numOfWaypoints && slotTranslation.get(key+1) >= numOfWaypoints) {
                return key;
            }
        }
        return 35;
    }
}
