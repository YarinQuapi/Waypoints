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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 */
public class WaypointBrowser extends AbstractGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.public.browser");
        this.setSlots(9*3);
        this.setTitle(Utils.newMessageNoPrefix("&7Waypoint Browser"));

        List<Waypoint> waypoints = Waypoints.getInstance().getWaypointHandler().getPublicWaypoints();

        if (waypoints.isEmpty()) {
            player.sendMessage(MessagesUtils.getMessage("gui.no-items"));
            return;
        }


        int i = 0;
        for (Waypoint wp : waypoints) {

            ItemStack itemStack = wp.getItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            Vector vec = wp.getVector();

            ArrayList<String> lore = new ArrayList<>();
            String coordinatesString = String.format(Utils.newMessageNoPrefix("&7Coordinates &bX &a%s &bY &a%s &bZ &a%s"), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            lore.add(coordinatesString);

            String waypointWorld = wp.getWorld();
            lore.add(waypointWorld);

            String waypointState = Utils.newMessageNoPrefix("&7State " + wp.getState().getState());
            lore.add(waypointState);

            String waypointBiome = wp.getBiome();
            lore.add(waypointBiome);

            lore.add("\n");

            lore.add(Utils.newMessageNoPrefix("&7Owned by &b" + Bukkit.getOfflinePlayer(wp.getOwner()).getName()));

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Utils.newMessageNoPrefix("&b" + wp.getName()));

            itemStack.setItemMeta(itemMeta);

            this.getItems().put(i, new GuiItem(i, itemStack));

            i++;
        }

        try {
            this.initializeInventory();
        } catch (InventoryDoesNotExistException ignored) { }
        catch (GuiNoItemException e) {
            player.sendMessage(MessagesUtils.getMessage("gui.no-items", player.getName()));
        }
        this.openPage(player, 1);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == this.getInventory()) {
            e.setCancelled(true);

            ItemStack item = e.getInventory().getItem(e.getRawSlot());

            if (e.getRawSlot() == 22) {
                GuiUtils.openInventory("gui.personal.profile", (Player) e.getWhoClicked());
                return;
            }

            if (e.getRawSlot() == this.getSlots()) {
                this.nextPage((Player) e.getWhoClicked());
            }

            if (item != null && !item.getType().equals(Material.AIR) && item.getItemMeta() != null) {
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());

                Player player = (Player) e.getWhoClicked();

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_MENU_SLOT) {
                    GuiUtils.openInventory("gui.personal.profile", player);
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_NEXT_SLOT) {
                    this.nextPage(player);
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_PREVIOUS_SLOT) {
                    this.previousPage(player);
                }

                if (item.getItemMeta().hasLore()) {

                    OfflinePlayer offlinePlayer;

                    String offlinePlayerName = ChatColor.stripColor(item.getItemMeta().getLore().get(5)).substring(9);

                    offlinePlayer = Bukkit.getOfflinePlayer(offlinePlayerName);

                    Waypoint wp;
                    wp = Waypoints.getInstance().getWaypointHandler().getPublicWaypoints().stream().filter(x -> x.getName().equals(name)).filter(y -> y.getOwner().equals(offlinePlayer.getUniqueId())).findAny().get();

                    switch (e.getClick()) {
                        case LEFT -> {
                            LocationData locationData = wp.getLocationData();

                            String msg = Utils.newMessage(String.format("&7Waypoint &b%s &7is located at &bX &a%s &bY &a%s &bZ &a%s &7in world &b%s &7You are &b%s &7blocks away.",
                                    name, locationData.x(), locationData.y(), locationData.z(), WaypointWorld.valueOf(locationData.world()).getName(), Utils.calculateDistance(player.getLocation().toVector(), wp.getVector())));
                            player.sendMessage(msg);
                        }

                        case RIGHT -> Waypoints.getInstance().getTrackerManager().track(player, wp, Waypoints.getInstance().getWaypointHandler().getPlayer(player).getETracker());
                    }
                }
            }
        }
    }
}
