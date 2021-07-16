package me.yarinlevi.waypoints.player.actionbar;

import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 */
public class ActionBarHandler implements Listener {
    private final Map<Player, Waypoint> tracked = new HashMap<>();

    public void track(Player player, Waypoint waypoint) {
        this.tracked.put(player, waypoint);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        if (tracked.containsKey(p)) {
            Waypoint wp = tracked.get(p);

            if (wp.get2DDistance() > 10) {
                int distance = wp.get2DDistance();

                Vector direction = wp.getLocation().clone().subtract(p.getLocation()).toVector().normalize();

                Vector cardinalDirectionVector = getClosestCardinalVectorFrom(direction);

                BlockFace face = Arrays.stream(cardinalVectors).filter(x -> x.getDirection().equals(cardinalDirectionVector)).findAny().get();

                p.sendMessage("Heading: " + face.name());

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Distance: " + distance + ", Heading: "));
            } else {
                p.sendMessage(Utils.newMessage("&7You've reached your destination!"));
                tracked.remove(p);
            }
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
        for(BlockFace cardinalVec : cardinalVectors) {
            double tempChangedDir = cardinalVec.getDirection().dot(movedDir);
            if(tempChangedDir >= TIPPING_LIMIT) return cardinalVec.getDirection();
            if(tempChangedDir > lowestAmountChangedDir) {
                lowestAmountChangedDir = tempChangedDir;
                closest = cardinalVec.getDirection();
            }
        }
        return closest;
    }
}
