package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;
import lombok.Setter;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * @author YarinQuapi
 */
public class Waypoint {
    @Getter @Setter private String name;
    @Getter private final Location location;
    @Getter private final boolean systemInduced;
    @Getter private ItemStack item = new ItemStack(Material.DIRT);
    @Getter private final WaypointWorld world;
    @Getter private final Player owner;

    public Waypoint(Player owner, String name, Location location, boolean systemInduced) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.systemInduced = systemInduced;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public Waypoint(Player owner, String name, Location location, ItemStack item, boolean systemInduced) {
        this.owner = owner;
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

    public String getFormattedCoordinates() {
        return Utils.newMessageNoPrefix(String.format("&bX &a%s &bY &a%s &bZ &a%s", getVector().getBlockX(), getVector().getBlockY(), getVector().getBlockZ()));
    }

    public int getDistance() {
        return Utils.calculateDistance(getVector(), owner.getLocation().toVector());
    }

    public LocationData getLocationData() {
        return new LocationData(String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ()),
                location.getWorld().getEnvironment().name(),
                location.getWorld().getChunkAt(location).isSlimeChunk());
    }
}
