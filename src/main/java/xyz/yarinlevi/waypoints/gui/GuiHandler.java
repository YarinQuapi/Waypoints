package xyz.yarinlevi.waypoints.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.gui.inventories.CreateWaypointGui;
import xyz.yarinlevi.waypoints.gui.inventories.ProfileGui;
import xyz.yarinlevi.waypoints.gui.inventories.WaypointListGui;

import java.util.HashMap;

public class GuiHandler implements Listener {
    private static final HashMap<String, IGui> guiList = new HashMap<>();

    public static void openInventory(String key, Player player) {
        guiList.getOrDefault(key, new ProfileGui()).run(player);
    }

    public static HashMap<String, IGui> registerGui() {
        guiList.put("gui.personal.profile", new ProfileGui());
        guiList.put("gui.personal.waypointlist", new WaypointListGui());
        guiList.put("gui.create.waypoint", new CreateWaypointGui());

        return guiList;
    }
}
