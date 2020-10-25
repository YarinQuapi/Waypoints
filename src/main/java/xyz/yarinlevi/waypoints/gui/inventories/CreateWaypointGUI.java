package xyz.yarinlevi.waypoints.gui.inventories;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class CreateWaypointGUI {
    public void open(Player player) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();

        builder.text("Please enter a name..");
        builder.open(player);
        builder.onComplete(((player1, text) -> {
            if(text.equalsIgnoreCase("ok")) {
                return AnvilGUI.Response.text("yes this is working");
            } else {
                return AnvilGUI.Response.text("if you wrote ok, it doesn't work, otherwise it works or try again.");
            }
        }));
    }
}
