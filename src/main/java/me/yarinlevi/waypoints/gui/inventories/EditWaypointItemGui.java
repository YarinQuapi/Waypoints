package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditWaypointItemGui {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (Material.getMaterial(text.toUpperCase()) != null) {
                        wp.editItem(new ItemStack(Material.getMaterial(text.toUpperCase())));
                        player2.sendMessage(Utils.newMessage("&eSuccessfully changed waypoint's item to: &d" + text.toUpperCase()));
                    } else {
                        player2.sendMessage(Utils.newMessage("&eThe item id was not found, please try again."));
                    }
                    return AnvilGUI.Response.close();
                })
                .text("Enter item id ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Edit waypoint")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}