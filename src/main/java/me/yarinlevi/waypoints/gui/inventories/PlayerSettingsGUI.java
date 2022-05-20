package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.GuiNoItemException;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.helpers.types.GuiItem;
import me.yarinlevi.waypoints.gui.items.Items;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * @author YarinQuapi
 **/
public class PlayerSettingsGUI extends AbstractGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.player-settings");
        this.setSlots(9*3);
        this.setTitle(MessagesUtils.getMessage("gui.player-settings.title"));

        PlayerData playerData = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId());

        /*
         * Player settings - profile
         */
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        // Statistics
        skullMeta.setDisplayName(MessagesUtils.getMessageFromData("gui.items.profile.title", player.getName()));
        skullMeta.setOwningPlayer(player);

        int overworldCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.NORMAL).size();
        int netherCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.NETHER).size();
        int endCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.THE_END).size();
        int systemInduced = Waypoints.getInstance().getWaypointHandler().getSystemInducedWaypointList(player).size();

        int total = overworldCount + netherCount + endCount + systemInduced;

        String lore = MessagesUtils.getMessageLines("gui.items.profile.lore", total, overworldCount, netherCount, endCount, systemInduced);

        skullMeta.setLore(List.of(lore));
        itemStack.setItemMeta(skullMeta);

        this.getItems().put(4, new GuiItem(4, itemStack));


        /*
         * Player settings - Tracker
         */
        ItemStack trackerItem = new ItemStack(Material.BARRIER);
        ItemMeta trackerMeta = trackerItem.getItemMeta();

        trackerMeta.setDisplayName(MessagesUtils.getMessage("gui.player-settings.tracker", playerData.getETracker().getKey()));

        trackerItem.setItemMeta(trackerMeta);

        this.getItems().put(10, new GuiItem(10, trackerItem));


        /*
         * Player settings - Deathpoints
         */
        ItemStack toggleDeathpointsItem = playerData.isPlayerDeathPoints() ? new ItemStack(Material.GREEN_WOOL) : new ItemStack(Material.RED_WOOL);
        ItemMeta toggleDeathpointsMeta = toggleDeathpointsItem.getItemMeta();

        toggleDeathpointsMeta.setDisplayName(MessagesUtils.getMessage("gui.player-settings.toggledeathpoints", playerData.isPlayerDeathPoints() ? MessagesUtils.getMessage("enabled") : MessagesUtils.getMessage("disabled")));

        toggleDeathpointsItem.setItemMeta(toggleDeathpointsMeta);

        this.getItems().put(16, new GuiItem(16, toggleDeathpointsItem));


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

                PlayerData playerData = Waypoints.getInstance().getPlayerDataManager().getPlayerDataMap().get(player.getUniqueId());

                if (e.getRawSlot() == 10) { // todo: find setting to put here lol.

                }

                if (e.getRawSlot() == 16) { // toggle deathpoints
                    boolean current = playerData.isPlayerDeathPoints();
                    boolean newValue = !current;

                    playerData.setPlayerDeathPoints(newValue);


                    ItemStack stateItem = newValue ? new ItemStack(Material.GREEN_WOOL) : new ItemStack(Material.RED_WOOL);
                    ItemMeta stateMeta = stateItem.getItemMeta();

                    stateMeta.setDisplayName(MessagesUtils.getMessage("gui.player-settings.toggledeathpoints", playerData.isPlayerDeathPoints() ? MessagesUtils.getMessage("enabled") : MessagesUtils.getMessage("disabled")));

                    stateItem.setItemMeta(stateMeta);

                    this.getInventory().setItem(13, stateItem);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.sendMessage(MessagesUtils.getMessage("setting_toggled", "DeathPoints", newValue));
                }

                if (e.getRawSlot() == 10) { // Change tracker
                    GuiUtils.openInventory("gui.personal.tracker", player);
                }

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_MENU_SLOT) { // Menu
                    GuiUtils.openInventory("gui.personal.profile", player);
                }
            }
        }
    }
}
