package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author YarinQuapi
 **/
public class DeleteWaypointGUI {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (text.trim().equalsIgnoreCase("confirm")) {
                        Waypoints.getInstance().getWaypointHandler().removeWaypoint(Bukkit.getOfflinePlayer(wp.getOwner()), wp);
                        player2.sendMessage(MessagesUtils.getMessage("waypoint_deleted", wp.getName()));
                    } else {
                        player2.sendMessage(MessagesUtils.getMessage("waypoint_delete_name_not_match"));
                    }

                    return AnvilGUI.Response.close();
                })
                .text("Type confirm.")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessageFromData("gui.delete.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
