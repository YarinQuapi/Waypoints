package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import xyz.yarinlevi.waypoints.gui.GuiHandler;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.Utils;
import xyz.yarinlevi.waypoints.waypoint.WaypointWorld;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileGui extends IGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.profile");
        this.setSlots(9*3);
        this.setTitle(Utils.newMessageNoPrefix("&bMain Profile: &d" + player.getName()));

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (skullMeta != null) {
            { // Statistics
                skullMeta.setDisplayName(Utils.newMessageNoPrefix("&d" + player.getDisplayName() + "&e's Stats"));
                skullMeta.setOwningPlayer(player);

                ArrayList<String> lore = new ArrayList<>();
                String waypointCount = String.format(Utils.newMessageNoPrefix("&eNumber of &bwaypoints&e: &d%s"), WaypointManager.tabCompleterList(player).size());
                lore.add(waypointCount);

                String waypointsInOverWorld = String.format(Utils.newMessageNoPrefix("&eWaypoints in &bOverworld&e: &d%s"), WaypointManager.listWaypointsInWorld(player, WaypointWorld.OVERWORLD).size());
                String waypointsInTheNether = String.format(Utils.newMessageNoPrefix("&eWaypoints in &bThe Nether&e: &d%s"), WaypointManager.listWaypointsInWorld(player, WaypointWorld.THE_NETHER).size());
                String waypointsInTheEnd = String.format(Utils.newMessageNoPrefix("&eWaypoints in &bThe End&e: &d%s"), WaypointManager.listWaypointsInWorld(player, WaypointWorld.THE_END).size());

                lore.add(waypointsInOverWorld);
                lore.add(waypointsInTheNether);
                lore.add(waypointsInTheEnd);

                skullMeta.setLore(lore);
                itemStack.setItemMeta(skullMeta);
                this.getItems().put(13, itemStack);
            }

            { // Create waypoint button
                ItemStack createWaypointButton = new ItemStack(Material.LIME_WOOL);
                ItemMeta meta = createWaypointButton.getItemMeta();

                assert meta != null;
                meta.setDisplayName(Utils.newRGBMessage("Create new Waypoint", 35, 245, 24));

                ArrayList<String> lore = new ArrayList<>();
                lore.add(Utils.newRGBMessage("Click here to create a new Waypoint!", 30, 219, 20));

                meta.setLore(lore);

                createWaypointButton.setItemMeta(meta);


                this.getItems().put(10, createWaypointButton);
            }

            { // Create waypoint button
                ItemStack waypointListButton = new ItemStack(Material.YELLOW_WOOL);
                ItemMeta meta = waypointListButton.getItemMeta();

                assert meta != null;
                meta.setDisplayName(Utils.newRGBMessage("List all waypoints", 255, 229, 28));

                ArrayList<String> lore = new ArrayList<>();
                lore.add(Utils.newRGBMessage("Click here to list all waypoints!", 255, 251, 0));

                meta.setLore(lore);

                waypointListButton.setItemMeta(meta);


                this.getItems().put(16, waypointListButton);
            }

            try {
                player.openInventory(this.initializeInventory());
            } catch (InventoryDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (Objects.equals(e.getInventory(), this.getInventory())) {
            e.setCancelled(true);
            if (e.getRawSlot() == 10) {
                GuiHandler.openInventory("gui.create.waypoint", player);
            } else if (e.getRawSlot() == 16) {
                GuiHandler.openInventory("gui.personal.waypointlist", player);
            }
        }
    }
}
