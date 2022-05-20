package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.GuiNoItemException;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.helpers.types.GuiItem;
import me.yarinlevi.waypoints.gui.items.Items;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.WaypointState;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YarinQuapi
 **/
public class WaypointSettingsGUI extends AbstractGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypoint-settings");
        this.setSlots(9*3);
        this.setTitle(MessagesUtils.getMessage("gui.waypoint-settings.title", this.getWaypoint().getName()));

        /*
         * Waypoint settings - profile
         */

        ItemStack itemStack = this.getWaypoint().getItem();
        ItemMeta meta = itemStack.getItemMeta();

        List<String> lore = new ArrayList<>();

        lore.add(this.getWaypoint().getWorld());
        lore.add(this.getWaypoint().getBiome());
        lore.add("\n");
        lore.add(this.getWaypoint().getFormattedCoordinates());
        lore.add("\n");
        lore.add(MessagesUtils.getMessage("click_edit_icon"));

        meta.setDisplayName("§b" + this.getWaypoint().getName());
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        this.getItems().put(4, new GuiItem(4, itemStack));


        /*
         * Waypoint settings - rename
         */
        ItemStack renameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameItem.getItemMeta();

        renameMeta.setDisplayName(MessagesUtils.getMessage("gui.waypoint-settings.rename"));

        renameItem.setItemMeta(renameMeta);

        this.getItems().put(10, new GuiItem(10, renameItem));


        /*
         * Waypoint settings - delete
         */
        ItemStack deleteItem = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = deleteItem.getItemMeta();

        deleteMeta.setDisplayName(MessagesUtils.getMessage("gui.waypoint-settings.delete"));

        deleteItem.setItemMeta(deleteMeta);

        this.getItems().put(16, new GuiItem(16, deleteItem));


        /*
         * Waypoint settings - state
         */
        ItemStack stateItem = this.getWaypoint().getState() == WaypointState.PUBLIC ? new ItemStack(Material.GREEN_WOOL) : new ItemStack(Material.RED_WOOL);
        ItemMeta stateMeta = stateItem.getItemMeta();

        stateMeta.setDisplayName(MessagesUtils.getMessage("gui.waypoint-settings.state", this.getWaypoint().getState().getState()));

        stateItem.setItemMeta(stateMeta);

        this.getItems().put(13, new GuiItem(13, stateItem));



        try {
            this.initializeInventory();
        } catch (InventoryDoesNotExistException ignored) { }
        catch (GuiNoItemException e) {
            player.sendMessage(MessagesUtils.getMessageFromData("gui.no-items", player.getName()));
        }
        this.openPage(player, 1);
    }

    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == this.getInventory()) {
            e.setCancelled(true);

            ItemStack item = e.getInventory().getItem(e.getRawSlot());

            if (item == null) {
                return;
            }

            if (!item.getType().equals(Material.AIR)) {
                Player player = (Player) e.getWhoClicked();

                if (e.getRawSlot() == 4) { // Edit icon
                    EditWaypointItemGui.open(player, this.getWaypoint());
                }

                if (e.getRawSlot() == 10) { // Rename
                    RenameWaypointGUI.open(player, this.getWaypoint());
                }

                if (e.getRawSlot() == 13) { // State
                    WaypointState currentState = this.getWaypoint().getState();
                    WaypointState newState = currentState == WaypointState.PUBLIC ? WaypointState.PRIVATE : WaypointState.PUBLIC;
                    this.getWaypoint().setState(newState);

                    try {
                        Waypoints.getInstance().getPlayerData().setWaypointState(this.getWaypoint().getOwner(), this.getWaypoint().getName(), newState);
                    } catch (WaypointDoesNotExistException ex) {
                        ex.printStackTrace();
                        player.sendMessage("Something went wrong with state change. this should never happen. if it happens, report it to the developer. thanks in advance.");
                        return;
                    }

                    ItemStack stateItem = newState == WaypointState.PUBLIC ? new ItemStack(Material.GREEN_WOOL) : new ItemStack(Material.RED_WOOL);
                    ItemMeta stateMeta = stateItem.getItemMeta();

                    stateMeta.setDisplayName(MessagesUtils.getMessage("gui.waypoint-settings.state", this.getWaypoint().getState().getState()));

                    stateItem.setItemMeta(stateMeta);

                    this.getInventory().setItem(13, stateItem);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.sendMessage(MessagesUtils.getMessage("state_change", newState.getState()));
                }

                if (e.getRawSlot() == 16) { // Delete
                    DeleteWaypointGUI.open(player, this.getWaypoint());
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_MENU_SLOT) { // Menu
                    GuiUtils.openInventory("gui.personal.profile", player);
                }
            }
        }
    }
}
