package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author YarinQuapi
 */
public class RenameWaypointGUI {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onClick((slot, state) -> {
                    Player player2 = state.getPlayer();
                    String text = state.getText();

                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (!Utils.disallowedCharacters.matcher(text.trim()).matches()) {
                            wp.setName(text.trim());
                            player2.sendMessage(Utils.newMessage("&7Successfully changed waypoint's name to: &b" + text.trim()));
                        } else {
                            player2.sendMessage(Utils.newMessage("&cRename failed! &7illegal characters were found."));
                        }
                    } else {
                        return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText(MessagesUtils.getMessage("gui.try_again")));
                    }
                    return Arrays.asList(AnvilGUI.ResponseAction.close());
                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessage("gui.edit.name.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
