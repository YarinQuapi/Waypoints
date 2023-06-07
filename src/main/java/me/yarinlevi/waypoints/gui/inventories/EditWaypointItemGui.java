package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author YarinQuapi
 */
public class EditWaypointItemGui {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onClick((slot, state) -> {
                    Player player2 = state.getPlayer();
                    String text = state.getText();

                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (Material.getMaterial(text.trim().toUpperCase()) != null && !text.trim().equalsIgnoreCase("air")) {
                            wp.editItem(new ItemStack(Material.getMaterial(text.trim().toUpperCase())));
                            player2.sendMessage(MessagesUtils.getMessage("edit_icon", text.trim().toUpperCase()));
                        } else {
                            player2.sendMessage(MessagesUtils.getMessage("edit_icon_failed"));
                        }
                    } else {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText(MessagesUtils.getMessage("gui.try_again")));
                    }

                    return List.of(AnvilGUI.ResponseAction.close());
                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessage("gui.edit.icon.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}