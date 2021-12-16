package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author YarinQuapi
 */
public class CreateWaypointGui extends AbstractGui {
    @Override
    public void run(Player player) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    if (Utils.allowedCharacters.matcher(text.trim()).matches()) {
                        Waypoint waypoint = new Waypoint(player.getUniqueId(), text.trim(), player2.getLocation(), false);
                        try {
                            Waypoints.getInstance().getWaypointHandler().addWaypoint(player2.getUniqueId(), waypoint);
                            player2.sendMessage(MessagesUtils.getMessage("waypoint_created", text.trim()));
                        } catch (WaypointAlreadyExistsException | PlayerNotLoadedException e) {
                            player2.sendMessage(e.getMessage());
                        }
                    } else {
                        player2.sendMessage(MessagesUtils.getMessage("illegal_characters"));
                    }
                    return AnvilGUI.Response.close();

                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessage("gui.create.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
