package me.yarinlevi.waypoints.events;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.exceptions.PlayerNotLoadedException;
import me.yarinlevi.waypoints.exceptions.WaypointAlreadyExistsException;
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

public class PlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        TextComponent msg = new TextComponent(Utils.newMessage("&eYou died. Created waypoint: "));
        TextComponent delete = new TextComponent(Utils.newMessageNoPrefix("&cDELETE"));

        int deathCount = p.getStatistic(Statistic.DEATHS);

        Waypoint waypoint = new Waypoint("Death-" + deathCount, p.getLocation(), true);

        try {
            if (Waypoints.getInstance().getWaypointHandler().addWaypoint(p, waypoint)) {
                TextComponent deathPoint = new TextComponent(Utils.newMessageNoPrefix("&f\"&dDeath-" + deathCount + "&f\" "));

                deathPoint.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&eClick to check waypoint &f\"&dDeath-%s&f\"", deathCount))).create()));
                deathPoint.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp check Death-" + deathCount));

                delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.newMessageNoPrefix(String.format("&eClick to delete waypoint &f\"&dDeath-%s&f\"", deathCount))).create()));
                delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wp delete Death-" + deathCount));

                msg.addExtra(deathPoint);

                msg.addExtra(delete);
                p.spigot().sendMessage(msg);
            }
        } catch (WaypointAlreadyExistsException | PlayerNotLoadedException ex) {
            p.sendMessage(ex.getMessage());
        }
    }
}
