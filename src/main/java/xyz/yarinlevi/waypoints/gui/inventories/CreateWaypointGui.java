package xyz.yarinlevi.waypoints.gui.inventories;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import xyz.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.Utils;
import xyz.yarinlevi.waypoints.waypoint.Waypoint;

public class CreateWaypointGui extends IGui {
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
                .text("Enter a name: ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Create waypoint")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
