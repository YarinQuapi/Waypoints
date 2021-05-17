package me.yarinlevi.waypoints.gui;

import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.inventories.CreateWaypointGui;
import me.yarinlevi.waypoints.gui.inventories.ProfileGui;
import me.yarinlevi.waypoints.gui.inventories.WaypointListGui;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GuiHandler implements Listener {
    private static final HashMap<String, Class<? extends AbstractGui>> guiList = new HashMap<>();

    public static void openInventory(String key, Player player) {
        try {
            guiList.getOrDefault(key, ProfileGui.class).newInstance().run(player);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Class<? extends AbstractGui>> registerGui() {
        guiList.put("gui.personal.profile", ProfileGui.class);
        guiList.put("gui.personal.waypointlist", WaypointListGui.class);
        guiList.put("gui.create.waypoint", CreateWaypointGui.class);

        return guiList;
    }
}
