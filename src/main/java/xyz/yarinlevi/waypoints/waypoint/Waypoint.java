package xyz.yarinlevi.waypoints.waypoint;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Waypoint {
    @Getter @Setter private String name;
    @Getter private final Location location;
    @Getter private final boolean systemInduced;
    @Getter private ItemStack item = new ItemStack(Material.DIRT);
    @Getter private final WaypointWorld world;

    public Waypoint(String name, Location location, boolean systemInduced) {
        this.name = name;
        this.location = location;
        this.systemInduced = systemInduced;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public Waypoint(String name, Location location, ItemStack item, boolean systemInduced) {
        this.name = name;
        this.location = location;
        this.item = item;
        this.systemInduced = systemInduced;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public void editItem(ItemStack item) {
        this.item = item;
    }

    public Vector getVector() {
        return this.location.toVector();
    }

    public static Waypoint createWaypoint(String name, Location location, boolean systemInduced) {
        return new Waypoint(name, location, systemInduced);
    }
}
