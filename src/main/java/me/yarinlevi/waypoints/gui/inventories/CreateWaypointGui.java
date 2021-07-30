package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import me.yarinlevi.waypoints.waypointData.FileManager;
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
                            FileManager.save(Waypoints.getInstance().getPlayerListener().getWaypointDataFile(), Waypoints.getInstance().getPlayerListener().getWaypointData());
                            player2.sendMessage(Utils.newMessage(String.format("&7Created new waypoint &b%s", text.trim())));
                        } catch (WaypointAlreadyExistsException | PlayerNotLoadedException e) {
                            player2.sendMessage(e.getMessage());
                        }
                    } else {
                        player2.sendMessage(Utils.newMessage("&cIllegal characters found (Allowed: A-z0-9)"));
                    }
                    return AnvilGUI.Response.close();

                })
                .text(" ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter a waypoint name")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
