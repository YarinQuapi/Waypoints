package me.yarinlevi.waypoints.gui.guilist;

import com.google.gson.JsonArray;
import lombok.Getter;
import me.yarinlevi.waypoints.data.Data;
import me.yarinlevi.waypoints.data.WaypointManager;
import me.yarinlevi.waypoints.utils.LocationHandler;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class ListWaypointsGui {
    @Getter private static Inventory listWaypointGui;

    public ListWaypointsGui() {
        listWaypointGui = Bukkit.createInventory(null, 27, Utils.newMessageNoPrefix("&b&lQWaypoints -> Waypoint list"));
    }

    public static void openWaypointList(Player p) {
        ArrayList<String> waypoints = new ArrayList<>(WaypointManager.tabCompleterList(p));
        if(!waypoints.isEmpty()) {
            for (String waypointName : waypoints) {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(Utils.newMessageNoPrefix("&b&lWaypoint name: &d%wp%".replaceAll("%wp%", waypointName)));
                HashMap<String, String> locDetail = LocationHandler.handleLocation((Location) Data.getWaypointData(p, waypointName).get("location"));
            }
        }
    }
}
