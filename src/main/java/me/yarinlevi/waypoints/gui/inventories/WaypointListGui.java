package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.GuiNoItemException;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.helpers.types.GuiItem;
import me.yarinlevi.waypoints.gui.items.Items;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
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

/**
 * @author YarinQuapi
 */
public class WaypointListGui extends AbstractGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypointlist");
        this.setSlots(9*4);
        this.setTitle(Utils.newMessageNoPrefix("&7Waypoint List"));

        if (Waypoints.getInstance().getWaypointHandler().getWaypointList(player).isEmpty()) {
            player.sendMessage(MessagesUtils.getMessageFromData("gui.no-items", player.getName()));
            return;
        }

        int i = 0;
        for (String waypointName : Waypoints.getInstance().getWaypointHandler().getWaypointList(player)) {
            Waypoint wp;
            wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, waypointName);

            if (wp != null) {

                ItemStack itemStack = wp.getItem();
                ItemMeta itemMeta = itemStack.getItemMeta();

                ArrayList<String> lore = new ArrayList<>();
                String coordinatesString = wp.getFormattedCoordinates();
                lore.add(coordinatesString);

                String waypointWorld = wp.getWorld();
                lore.add(waypointWorld);

                String waypointState = Utils.newMessageNoPrefix("&7State " + wp.getState().getState());
                lore.add(waypointState);

                String waypointBiome = wp.getBiome();
                lore.add(waypointBiome);

                lore.add("\n");

                String rightClickToEdit = Utils.newMessageNoPrefix("&eRight click to open settings!");
                lore.add(rightClickToEdit);

                String leftClickToEdit = Utils.newMessageNoPrefix("&eLeft click to display in chat!");
                lore.add(leftClickToEdit);

                itemMeta.setLore(lore);
                itemMeta.setDisplayName(Utils.newMessageNoPrefix("&b" + waypointName));

                itemStack.setItemMeta(itemMeta);

                this.getItems().put(i, new GuiItem(i, itemStack));

                i++;
            }
        }

        try {
            this.initializeInventory();
        } catch (InventoryDoesNotExistException ignored) { }
        catch (GuiNoItemException e) {
            player.sendMessage(MessagesUtils.getMessageFromData("gui.no-items", player.getName()));
        }
        this.openPage(player, 1);
    }

    @Override
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == this.getInventory()) {
            e.setCancelled(true);

            ItemStack item = e.getInventory().getItem(e.getRawSlot());

            if (item == null) {
                return;
            }

            if (!item.getType().equals(Material.AIR)) {
                Player player = (Player) e.getWhoClicked();

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_MENU_SLOT) {
                    GuiUtils.openInventory("gui.personal.profile", (Player) e.getWhoClicked());
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_NEXT_SLOT) {
                    this.nextPage(player);
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_PREVIOUS_SLOT) {
                    this.previousPage(player);
                }

                assert item.getItemMeta() != null;
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());

                Waypoint wp = Waypoints.getInstance().getWaypointHandler().getWaypoint(player, name);

                if (wp != null) {
                    switch (e.getClick()) {
                        case RIGHT -> {
                            GuiUtils.openInventory("gui.personal.waypoint-settings", player, wp);
                        }
                        case LEFT -> {
                            LocationData locationData = wp.getLocationData();

                            String msg = Utils.newMessage(String.format("&7Waypoint &b%s &7is located at &bX &a%s &bY &a%s &bZ &a%s &7in world &b%s &7You are &b%s &7blocks away.",
                                    name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), Utils.calculateDistance(player.getLocation().toVector(), wp.getVector())));
                            player.sendMessage(msg);

                            player.closeInventory();
                        }
                    }
                }
            }
        }
    }
}
