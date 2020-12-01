package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.ArrayList;

public class ProfileGui extends IGui {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.profile");
        this.setSlots(9*3);
        this.setTitle("profile gui test abstract");

        this.getItems().put(9, new ItemStack(Material.DIAMOND_AXE));
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (skullMeta != null) {
            { // replace with waypoint list something gay.
                skullMeta.setDisplayName(player.getDisplayName() + "'s Profile");
                skullMeta.setOwningPlayer(player);

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


                this.getItems().put(11, createWaypointButton);
            }

            { // Create waypoint button
                ItemStack deleteWaypointButton = new ItemStack(Material.RED_WOOL);
                ItemMeta meta = deleteWaypointButton.getItemMeta();

                assert meta != null;
                meta.setDisplayName(Utils.newRGBMessage("Delete a Waypoint", 246, 30, 16));

                ArrayList<String> lore = new ArrayList<>();
                lore.add(Utils.newRGBMessage("Click here to delete a Waypoint!", 246, 37, 60));

                meta.setLore(lore);

                deleteWaypointButton.setItemMeta(meta);


                this.getItems().put(16, deleteWaypointButton);
            }


            try {
                player.openInventory(this.initializeInventory());
            } catch (InventoryDoesNotExistException e) {
                e.printStackTrace();
            }
            player.sendMessage("opened a gui for you.");
        }
    }
}
