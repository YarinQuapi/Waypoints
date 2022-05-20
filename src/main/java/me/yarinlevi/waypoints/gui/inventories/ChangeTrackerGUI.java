package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.helpers.types.GuiItem;
import me.yarinlevi.waypoints.gui.items.Items;
import me.yarinlevi.waypoints.player.PlayerData;
import me.yarinlevi.waypoints.player.trackers.ETracker;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author YarinQuapi
 * Waypoint tracker setting GUI, opened from @see { @link me.yarinlevi.waypoints.gui.inventories.PlayerSettingsGUI }
 **/
public class ChangeTrackerGUI extends AbstractGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.tracker");
        this.setSlots(9*3);
        this.setTitle(MessagesUtils.getMessage("gui.tracker.title"));

        ItemStack actionBarItem = new ItemStack(Material.BOOK);
        ItemMeta actionBarMeta = actionBarItem.getItemMeta();

        actionBarMeta.setDisplayName(MessagesUtils.getMessage("gui.tracker.actionbar"));

        actionBarItem.setItemMeta(actionBarMeta);
        this.getItems().put(12, new GuiItem(12, actionBarItem));


        ItemStack bossBarItem = new ItemStack(Material.COMPASS);
        ItemMeta bossBarMeta = bossBarItem.getItemMeta();

        bossBarMeta.setDisplayName(MessagesUtils.getMessage("gui.tracker.bossbar"));

        bossBarItem.setItemMeta(bossBarMeta);
        this.getItems().put(13, new GuiItem(13, bossBarItem));


        ItemStack particleItem = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta particleMeta = particleItem.getItemMeta();

        particleMeta.setDisplayName(MessagesUtils.getMessage("gui.tracker.particle"));

        particleItem.setItemMeta(particleMeta);
        this.getItems().put(14, new GuiItem(14, particleItem));
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

                if (e.getRawSlot() == 12) {
                    playerData.setETracker(ETracker.ActionBar);
                }
                if (e.getRawSlot() == 13) {
                    playerData.setETracker(ETracker.BossBar);
                }
                if (e.getRawSlot() == 14) {
                    playerData.setETracker(ETracker.Particle);
                }

                player.sendMessage(MessagesUtils.getMessage("tracker_changed", playerData.getETracker().getKey()));
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                player.closeInventory();

                if (e.getRawSlot() == this.getSlots() -9+ Items.ITEM_MENU_SLOT) { // Menu
                    GuiUtils.openInventory("gui.personal.profile", player);
                }
            }
        }
    }
}