package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;

public class WaypointListGui extends AbstractGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypointlist");
        this.setSlots(9*3);
        this.setTitle(Utils.newMessageNoPrefix("&7Waypoint List"));

        ItemStack backItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = backItem.getItemMeta();

        meta.setDisplayName(Utils.newMessageNoPrefix("&6&lBack to menu"));
        meta.setLore(Collections.singletonList(Utils.newMessageNoPrefix("&6Click to go back.")));

        backItem.setItemMeta(meta);
        this.getItems().put(22, backItem);


        int i = 0;
        for (String waypointName : Waypoints.getInstance().getWaypointHandler().getWaypointList(player)) {
            Waypoint wp;
            wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypointName);

            if (wp != null) {

                ItemStack itemStack = wp.getItem();
                ItemMeta itemMeta = itemStack.getItemMeta();

                Vector vec = wp.getVector();

                ArrayList<String> lore = new ArrayList<>();
                String coordinatesString = String.format(Utils.newMessageNoPrefix("&7Coordinates &bX &a%s &bY &a%s &bZ &a%s"), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
                lore.add(coordinatesString);

                String waypointWorld = String.format(Utils.newMessageNoPrefix("&7World &a%s"), wp.getWorld().getName());
                lore.add(waypointWorld);

                lore.add("\n");

                String rightClickToEdit = Utils.newMessageNoPrefix("&7Right click to edit item!");
                lore.add(rightClickToEdit);

                itemMeta.setLore(lore);
                itemMeta.setDisplayName(Utils.newMessageNoPrefix("&b" + waypointName));

                itemStack.setItemMeta(itemMeta);

                this.getItems().put(i, itemStack);
                i++;
                if (i == 22) {
                    i++;
                }
            }
        }

        try {
            player.openInventory(this.initializeInventory());
        } catch (InventoryDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == this.getInventory()) {
            if (e.getClick().isRightClick()) {
                ItemStack item = e.getInventory().getItem(e.getRawSlot());

                assert item != null;
                assert item.getItemMeta() != null;
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());

                Player player = (Player) e.getWhoClicked();

                Waypoint wp;
                wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, name);

                if (wp != null) {
                    EditWaypointItemGui.open(player, wp);
                }
            }

            if (e.getRawSlot() == 22) {
                GuiUtils.openInventory("gui.personal.profile", (Player) e.getWhoClicked());
            }
        }
    }
}
