package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author YarinQuapi
 */
public class CreateWaypointGui extends AbstractGui {
    @Override
    public void run(Player player) {
        new AnvilGUI.Builder()
                .onClick((slot, state) -> {
                    String text = state.getText();
                    Player player2 = state.getPlayer();

                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (!Utils.disallowedCharacters.matcher(text.trim()).matches() && !text.trim().equalsIgnoreCase("air")) {
                            Waypoint waypoint = new Waypoint(player.getUniqueId(), text.trim(), player2.getLocation(), false);
                            try {
                                Waypoints.getInstance().getWaypointHandler().addWaypoint(player2, waypoint);
                                player2.sendMessage(MessagesUtils.getMessage("waypoint_created", text.trim()));
                            } catch (WaypointAlreadyExistsException | WaypointLimitReachedException |
                                     PlayerNotLoadedException e) {
                                player2.sendMessage(e.getMessage());
                            }
                        } else {
                            return List.of(AnvilGUI.ResponseAction.replaceInputText(MessagesUtils.getMessage("illegal_characters")));
                        }
                    } else {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText(MessagesUtils.getMessage("gui.try_again")));
                    }

                    return List.of(AnvilGUI.ResponseAction.close());

                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(MessagesUtils.getMessage("gui.create.title"))
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
