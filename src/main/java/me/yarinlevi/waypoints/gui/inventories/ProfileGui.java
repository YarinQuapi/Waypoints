package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import me.yarinlevi.waypoints.gui.GuiUtils;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.helpers.types.GuiItem;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.WaypointWorld;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author YarinQuapi
 */
public class ProfileGui extends AbstractGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.profile");
        this.setSlots(9*3);
        this.setTitle(Utils.newMessageNoPrefix("&7Main Profile: &b" + player.getName()));


        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        // Statistics
        skullMeta.setDisplayName(Utils.newMessageNoPrefix("&d" + player.getDisplayName() + "&7's Stats"));
        skullMeta.setOwningPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        String waypointCount = String.format(Utils.newMessageNoPrefix("&b%s &7Waypoints"), Waypoints.getInstance().getWaypointHandler().getWaypointList(player).size());
        lore.add(waypointCount);

        int overworldCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.NORMAL).size();
        int netherCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.NETHER).size();
        int endCount = Waypoints.getInstance().getWaypointHandler().getWaypointList(player, WaypointWorld.THE_END).size();
        int systemInduced = Waypoints.getInstance().getWaypointHandler().getSystemInducedWaypointList(player).size();

        if (overworldCount > 0) {
            lore.add(String.format(Utils.newMessageNoPrefix("&a%s &7Waypoints in &bOverworld"), overworldCount));
        }

        if (netherCount > 0) {
            lore.add(String.format(Utils.newMessageNoPrefix("&a%s &7Waypoints in &bThe Nether"), netherCount));
        }

        if (endCount > 0) {
            lore.add(String.format(Utils.newMessageNoPrefix("&a%s &7Waypoints in &bThe End"), endCount));
        }

        if (systemInduced > 0) {
            lore.add(String.format(Utils.newMessageNoPrefix("&4%s &cDeathpoints"), systemInduced));
        }

        skullMeta.setLore(lore);
        itemStack.setItemMeta(skullMeta);
        this.getItems().put(13, new GuiItem(13, itemStack));

        // Create waypoint button
        ItemStack createWaypointButton = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta2 = createWaypointButton.getItemMeta();

        meta2.setDisplayName(Utils.newRGBMessage("Create a new waypoint", new Utils.RGBController(35, 245, 24)));

        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(Utils.newRGBMessage("Click here to create a new waypoint!", new Utils.RGBController(30, 219, 20)));

        meta2.setLore(lore2);

        createWaypointButton.setItemMeta(meta2);


        this.getItems().put(10, new GuiItem(10, createWaypointButton));

        // Create waypoint button
        ItemStack waypointListButton = new ItemStack(Material.YELLOW_WOOL);
        ItemMeta meta1 = waypointListButton.getItemMeta();

        assert meta1 != null;
        meta1.setDisplayName(Utils.newRGBMessage("List all waypoints", new Utils.RGBController(255, 205, 28)));

        ArrayList<String> lore1 = new ArrayList<>();
        lore1.add(Utils.newRGBMessage("Click here to list all waypoints!", new Utils.RGBController(255, 251, 0)));

        meta1.setLore(lore1);

        waypointListButton.setItemMeta(meta1);


        this.getItems().put(16, new GuiItem(16, waypointListButton));


        try {
            this.initializeInventory();
        } catch (InventoryDoesNotExistException ignored) { }
        this.openPage(player, 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (Objects.equals(e.getInventory(), this.getInventory())) {

            e.setCancelled(true);
            if (e.getRawSlot() == 10) {
                GuiUtils.openInventory("gui.create.waypoint", player);
            } else if (e.getRawSlot() == 16) {
                GuiUtils.openInventory("gui.personal.waypointlist", player);
            }
        }
    }
}
