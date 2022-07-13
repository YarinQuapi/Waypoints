package me.yarinlevi.waypoints.waypoint;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.WaypointDoesNotExistException;
import me.yarinlevi.waypoints.utils.LocationData;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.types.StateIdentifier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * @author YarinQuapi
 */
public class Waypoint {
    @Getter private String name;
    @Getter private final Location location;
    @Getter private final boolean deathpoint;
    @Getter private ItemStack item = new ItemStack(Material.DIRT);
    private final WaypointWorld world;
    @Getter private final UUID owner;
    @Getter @Setter private StateIdentifier state = StateIdentifier.PRIVATE;

    public Waypoint(UUID owner, String name, Location location, boolean deathpoints) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.deathpoint = deathpoints;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public Waypoint(UUID owner, String name, Location location, ItemStack item, boolean deathpoints) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.item = item;
        this.deathpoint = deathpoints;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public Waypoint(UUID owner, String name, Location location, StateIdentifier state, boolean deathpoints) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.deathpoint = deathpoints;
        this.state = state;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public Waypoint(UUID owner, String name, Location location, ItemStack item, StateIdentifier state, boolean deathpoints) {
        this.owner = owner;
        this.name = name;
        this.location = location;
        this.item = item;
        this.deathpoint = deathpoints;
        this.state = state;
        world = WaypointWorld.valueOf(location.getWorld().getEnvironment().name());
    }

    public void editItem(ItemStack item) {
        this.item = item;

        Waypoints.getInstance().getPlayerData().updateWaypointItem(this.owner, this.name, item.getType().name().toUpperCase());
    }

    public Vector getVector() {
        return this.location.toVector();
    }

    public void teleportToWaypoint(Player player) {
        player.teleport(this.location);
    }

    /**
     * Only run if player is online, check online status on YOUR side!
     * @return distance between the waypoint and the player
     */
    public int getDistance() {
        return Utils.calculateDistance(getVector(), Bukkit.getPlayer(owner).getLocation().toVector());
    }

    public int getDistance(Player player) {
        return Utils.calculateDistance(getVector(), player.getLocation().toVector());
    }

    public String getFormattedCoordinates() {
        return MessagesUtils.getMessage("coordinates_format", getVector().getBlockX(), getVector().getBlockY(), getVector().getBlockZ());
    }

    public String getBiome() {
        return MessagesUtils.getMessage("biome_format", location.getBlock().getBiome().name());
    }

    public String getWorld() {
        return MessagesUtils.getMessage("world_format", this.world.name);
    }

    public WaypointWorld getWorldIdentifier() {
        return world;
    }

    /**
     * Only run if player is online, check online status on YOUR side!
     * @return distance between the waypoint and the player
     */
    public int get2DDistance() {
        return Utils.calculate2DDistance(getVector(), Bukkit.getPlayer(owner).getLocation().toVector());
    }


    @NonNull
    public LocationData getLocationData() {
        return new LocationData(String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ()),
                location.getWorld().getEnvironment().name());
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;

        try {
            Waypoints.getInstance().getPlayerData().renameWaypoint(this.owner, oldName, name);
        } catch (WaypointDoesNotExistException e) {
            e.printStackTrace();
        }
    }
}
