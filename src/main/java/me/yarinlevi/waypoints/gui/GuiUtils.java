package me.yarinlevi.waypoints.gui;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.gui.helpers.AbstractGui;
import me.yarinlevi.waypoints.gui.inventories.CreateWaypointGui;
import me.yarinlevi.waypoints.gui.inventories.ProfileGui;
import me.yarinlevi.waypoints.gui.inventories.WaypointListGui;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

/**
 * @author YarinQuapi
 */
public class GuiUtils implements Listener {
    private static final HashMap<String, Class<? extends AbstractGui>> guiList = new HashMap<>();

    public static void openInventory(String key, Player player) {

        player.closeInventory();

        try {
            AbstractGui gui = guiList.getOrDefault(key, ProfileGui.class).newInstance();

            Waypoints.getInstance().getServer().getPluginManager().registerEvents(gui, Waypoints.getInstance());

            gui.run(player);
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
