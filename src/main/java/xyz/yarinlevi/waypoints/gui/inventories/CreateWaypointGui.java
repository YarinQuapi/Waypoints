package xyz.yarinlevi.waypoints.gui.inventories;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yarinlevi.waypoints.Waypoints;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;

public class CreateWaypointGui extends IGui {
    @Override
    public void run(Player player) {
        new AnvilGUI.Builder()
                .onComplete((player2, text) -> {
                    WaypointManager.addWaypoint(player2, text, false);
                    return AnvilGUI.Response.close();
                })
                .text("Enter a name: ")
                .itemLeft(new ItemStack(Material.PAPER))
                .title("Create waypoint")
                .plugin(Waypoints.getInstance())
                .open(player);
    }
}
