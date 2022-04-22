package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author YarinQuapi
 */
public class EditWaypointItemGui {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (Material.getMaterial(text.trim().toUpperCase()) != null) {
                        wp.editItem(new ItemStack(Material.getMaterial(text.trim().toUpperCase())));
                        player2.sendMessage(MessagesUtils.getMessage("edit_icon", text.trim().toUpperCase()));
                    } else {
                        player2.sendMessage(MessagesUtils.getMessage("edit_icon_failed"));
                    }
                    return AnvilGUI.Response.close();
                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessageFromData("gui.edit.icon.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}