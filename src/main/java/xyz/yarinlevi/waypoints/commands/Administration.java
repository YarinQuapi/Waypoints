package xyz.yarinlevi.waypoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.yarinlevi.waypoints.gui.helpers.Gui;
import xyz.yarinlevi.waypoints.gui.inventories.CreateWaypointGUI;
import xyz.yarinlevi.waypoints.gui.inventories.ListWaypointsGui;
import xyz.yarinlevi.waypoints.utils.LocationHandler;
import xyz.yarinlevi.waypoints.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class Administration implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final Player p = (Player)sender;

        if(args.length == 0) {
            p.sendMessage(Utils.newMessage("&eMissing arguments."));
            return false;
        }

        if(args[0].equalsIgnoreCase("chunkscan")) {
            HashMap<String, String> locDetail = LocationHandler.handleLocation(p.getLocation());
            String msg = Utils.newMessage("&eAdvanced chunk scan:\n" +
                    String.format("&b  • &eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s\n", locDetail.get("x"), locDetail.get("y"), locDetail.get("z")) +
                    String.format("&b  • &eIs Slime Chunk?: &d%s\n", locDetail.get("isSlimeChunk")) +
                    String.format("&b  • &eWorld: &d%s", locDetail.get("world")));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("spawn")) {
            HashMap<String, String> locDetail = LocationHandler.handleLocation(p.getWorld().getSpawnLocation());
            String msg = Utils.newMessage("&eSpawn locator:\n" +
                    String.format("&b  • &eCoordinates: &bX&e: &d%s &bY&e: &d%s &bZ&e: &d%s\n", locDetail.get("x"), locDetail.get("y"), locDetail.get("z")));
            p.sendMessage(msg);
        } else if (args[0].equalsIgnoreCase("gui")) {
            if (args[1].equalsIgnoreCase("main")) {
                p.openInventory(Gui.getGui("WAYPOINTS_MAIN_GUI"));
            } else if (args[1].equalsIgnoreCase("WAYPOINTS_PERSONAL")) {
                ListWaypointsGui listWaypointsGui = new ListWaypointsGui();
                listWaypointsGui.initialize(p).register();
                p.openInventory(listWaypointsGui.getInventory());
            } else if (args[1].equalsIgnoreCase("ANVIL_GUI_CREATE")) {
                CreateWaypointGUI createWaypointGUI = new CreateWaypointGUI();
                createWaypointGUI.open(p);
            } else {
                if (Gui.guiList.containsKey(args[1].toUpperCase())) {
                    p.openInventory(Gui.getGui(args[1].toUpperCase()));
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }
}
