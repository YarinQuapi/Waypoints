package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author YarinQuapi
 **/
public class DeleteWaypointGUI {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onClick((slot, state) -> {
                    Player player2 = state.getPlayer();
                    String text = state.getText();

                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (text.trim().equalsIgnoreCase("confirm")) {
                            try {
                                Waypoints.getInstance().getWaypointHandler().removeWaypoint(Bukkit.getOfflinePlayer(wp.getOwner()), wp);
                            } catch (PlayerNotLoadedException e) {
                                throw new RuntimeException(e);
                            }
                            player2.sendMessage(MessagesUtils.getMessage("waypoint_deleted", wp.getName()));
                        } else {
                            player2.sendMessage(MessagesUtils.getMessage("waypoint_delete_name_not_match"));
                        }
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(MessagesUtils.getMessage("gui.try_again")));
                    }

                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text("Type confirm.")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessage("gui.delete.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
