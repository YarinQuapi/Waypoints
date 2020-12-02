package xyz.yarinlevi.waypoints.gui.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import xyz.yarinlevi.waypoints.data.Data;
import xyz.yarinlevi.waypoints.data.WaypointManager;
import xyz.yarinlevi.waypoints.exceptions.InventoryDoesNotExistException;
import xyz.yarinlevi.waypoints.gui.GuiHandler;
import xyz.yarinlevi.waypoints.gui.helpers.IGui;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class WaypointListGui extends IGui implements Listener {
    @Override
    public void run(Player player) {
        this.setKey("gui.personal.waypointlist");
        this.setSlots(9*3);
        this.setTitle(Utils.newMessageNoPrefix("&b&lWaypoint List"));

        ItemStack backItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = backItem.getItemMeta();

        meta.setDisplayName(Utils.newMessageNoPrefix("&6&lBack to menu"));
        meta.setLore(Collections.singletonList(Utils.newMessageNoPrefix("&6Click to go back.")));

        backItem.setItemMeta(meta);
        this.getItems().put(22, backItem);


        int i = 0;
        for (String waypointName : WaypointManager.tabCompleterList(player)) {
            ConfigurationSection wp = Data.getWaypointData(player, waypointName);

            ItemStack itemStack = new ItemStack(Material.valueOf(wp.getString("item", "DIRT")));
            ItemMeta itemMeta = itemStack.getItemMeta();

            Location loc = (Location) wp.get("location");
            Vector vec = loc.toVector();

            ArrayList<String> lore = new ArrayList<>();
            String coordinatesString = String.format(Utils.newMessageNoPrefix("&eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s"), vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            lore.add(coordinatesString);

            String waypointWorld = String.format(Utils.newMessageNoPrefix("&eWorld: &d%s"), translateWorld(loc.getWorld().getEnvironment().name()));
            lore.add(waypointWorld);

            lore.add("\n");

            String rightClickToEdit = Utils.newMessageNoPrefix("&bRight click to edit item!");
            lore.add(rightClickToEdit);

            itemMeta.setLore(lore);
            itemMeta.setDisplayName(Utils.newMessageNoPrefix("&d" + waypointName));

            itemStack.setItemMeta(itemMeta);

            this.getItems().put(i, itemStack);
            i++;
            if (i == 22) {
                i++;
            }
        }

        try {
            player.openInventory(this.initializeInventory());
        } catch (InventoryDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getInventory() == this.getInventory()) {
            if (e.getClick().isRightClick()) {
                ItemStack item = e.getInventory().getItem(e.getRawSlot());

                assert item != null;
                assert item.getItemMeta() != null;
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());

                Player player = (Player) e.getWhoClicked();

                ConfigurationSection wp = Data.getWaypointData(player, name);

                EditWaypointItemGui.open(player, wp);
            }

            if (e.getRawSlot() == 22) {
                GuiHandler.openInventory("gui.personal.profile", (Player) e.getWhoClicked());
            }
        }
    }

    private String translateWorld(String world) {
        switch (world.toUpperCase()) {
            case "NORMAL":
                return "Overworld";
            case "NETHER":
                return "The Nether";
            case "THE_END":
                return "The End";
            default:
                return "Unknown world type";
        }
    }
}
