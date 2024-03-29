package me.yarinlevi.waypoints.listeners;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
import me.yarinlevi.waypoints.exceptions.WaypointLimitReachedException;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import me.yarinlevi.waypoints.utils.Utils;
import me.yarinlevi.waypoints.waypoint.Waypoint;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author YarinQuapi
 */
public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        //todo: fix settings being completely private and inaccessible
        //if (Waypoints.getInstance().getWaypointHandler().get(p.getUniqueId()).isPlayerDeathPoints()) {
            TextComponent msg = new TextComponent(MessagesUtils.getMessage("you_died"));
            TextComponent delete = new TextComponent(MessagesUtils.getMessage("delete"));

            int deathCount = p.getStatistic(Statistic.DEATHS);

            Waypoint waypoint = new Waypoint(p.getUniqueId(), "Death-" + deathCount, p.getLocation(), true);

            try {
                Waypoints.getInstance().getWaypointHandler().addWaypoint(p, waypoint);
                TextComponent deathPoint = new TextComponent(Utils.newMessageNoPrefix("&bDeath-" + deathCount));

                deathPoint.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&eClick to check waypoint &bDeath-%s", deathCount))).create()));
                deathPoint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check Death-" + deathCount));

                delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&7Click to delete waypoint &bDeath-%s", deathCount))).create()));
                delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp delete Death-" + deathCount));

                msg.addExtra(deathPoint);
                msg.addExtra(" ");
                msg.addExtra(delete);
                p.spigot().sendMessage(msg);
            } catch (WaypointAlreadyExistsException | WaypointLimitReachedException | PlayerNotLoadedException ex) {
                p.sendMessage(ex.getMessage());
            }
        //}
    }
}
