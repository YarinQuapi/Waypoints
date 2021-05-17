package me.yarinlevi.waypoints.gui.inventories;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateWaypointGui extends AbstractGui {
    @Override
    public void run(Player player) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    Waypoint waypoint = new Waypoint(text, player2.getLocation(), false);
                    try {
                        Waypoints.getInstance().getWaypointHandler().addWaypoint(player2, waypoint);
                        player2.sendMessage(Utils.newMessage(String.format("&eCreated new Waypoint: &f\"&d%s&f\"", text)));
                    } catch (WaypointAlreadyExistsException | PlayerNotLoadedException e) {
                        player2.sendMessage(e.getMessage());
                    }
                    return AnvilGUI.Response.close();
                })
                .text("")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Enter a waypoint name")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
