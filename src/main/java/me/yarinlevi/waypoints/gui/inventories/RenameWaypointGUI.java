package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author YarinQuapi
 */
public class RenameWaypointGUI {
    public static void open(Player player, Waypoint wp) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (Utils.allowedCharacters.matcher(text.trim()).matches()) {
                        wp.setName(text.trim());
                        player2.sendMessage(Utils.newMessage("&7Successfully changed waypoint's name to: &b" + text.trim()));
                    } else {
                        player2.sendMessage(Utils.newMessage("&cRename failed! &7illegal characters were found."));

                    }
                    return AnvilGUI.Response.close();
                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Rename waypoint")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
