package xyz.yarinlevi.waypoints.gui.inventories;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.utils.Utils;

public class EditWaypointItemGui {
    public static void open(Player player, ConfigurationSection wp) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (Material.getMaterial(text.toUpperCase()) != null) {
                        wp.set("item", text.toUpperCase());
                        Data.saveData();
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