package me.yarinlevi.waypoints.player.trackers;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class oldActionBarHandler implements Listener {
    private final Map<Player, Waypoint> tracked = new HashMap<>();

    public void track(Player player, Waypoint waypoint) {
        this.tracked.put(player, waypoint);
    }

    public boolean unTrack(Player player) {
        if (this.tracked.containsKey(player)) {
            this.tracked.remove(player);
            return true;
        } else return false;
    }

    private int tick = 0;

    public void update() {
        ++tick;
        if (tick == 40) {
            tick = 0;

            Bukkit.getScheduler().runTaskAsynchronously(Waypoints.getInstance(), () -> tracked.forEach((p, wp) -> {
                if (wp.get2DDistance() > 10) {
                    int distance = wp.get2DDistance();

                    Vector direction = wp.getLocation().clone().subtract(p.getLocation()).toVector().normalize();

                    Vector cardinalDirectionVector = getClosestCardinalVectorFrom(direction);

                    BlockFace face = Arrays.stream(cardinalVectors).filter(x -> x.getDirection().equals(cardinalDirectionVector)).findAny().get();

                    String cardinalDirection = translateCardinalDirection(face);

                    Bukkit.getScheduler().runTask(Waypoints.getInstance(), () -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(distance + "m, direction -> " + cardinalDirection)));
                } else {
                    Bukkit.getScheduler().runTask(Waypoints.getInstance(), () -> tracked.remove(p));
                    p.sendMessage(Utils.newMessage("&7You've reached your destination!"));
                }
            }));
        }
    }

    private String translateCardinalDirection(BlockFace face) {
        switch (face) {
            case NORTH_WEST -> { return "NW"; }
            case NORTH -> { return "N"; }
            case NORTH_EAST -> { return "NE"; }

            case EAST -> { return "E"; }

            case SOUTH_EAST -> { return "SE"; }
            case SOUTH -> { return "S"; }
            case SOUTH_WEST -> { return "SW"; }

            case WEST -> { return "W"; }

            default -> { return "ERROR, PLEASE REPORT"; }
        }
    }

    private static final BlockFace[] cardinalVectors = {
            BlockFace.NORTH_WEST,

            BlockFace.NORTH,

            BlockFace.NORTH_EAST,

            BlockFace.EAST,

            BlockFace.SOUTH_EAST,

            BlockFace.SOUTH,

            BlockFace.SOUTH_WEST,

            BlockFace.WEST
    };

    private static final double TIPPING_LIMIT = 0.5;

    public Vector getClosestCardinalVectorFrom(Vector movedDir) {
        Vector closest = null;
        double lowestAmountChangedDir = -Double.MIN_VALUE;
        for (BlockFace cardinalVec : cardinalVectors) {
            double tempChangedDir = cardinalVec.getDirection().dot(movedDir);
            if (tempChangedDir >= TIPPING_LIMIT) return cardinalVec.getDirection();
            if (tempChangedDir > lowestAmountChangedDir) {
                lowestAmountChangedDir = tempChangedDir;
                closest = cardinalVec.getDirection();
            }
        }
        return closest;
    }
}
