package me.yarinlevi.waypoints.gui.helpers;

import me.yarinlevi.waypoints.gui.GuiUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author YarinQuapi
 */
public class InventoryCloseListener implements Listener {
    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent event) {
        Player p = (Player) event.getPlayer();

        if (GuiUtils.getUnregisterNext().containsKey(p)) {
            HandlerList.unregisterAll(GuiUtils.getUnregisterNext().get(p));

            GuiUtils.getUnregisterNext().remove(p);
        }
    }
}
