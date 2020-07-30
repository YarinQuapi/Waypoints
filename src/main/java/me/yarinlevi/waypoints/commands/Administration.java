package me.yarinlevi.waypoints.commands;

import me.yarinlevi.waypoints.utils.LocationHandler;
import me.yarinlevi.waypoints.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

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
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        return null;
    }
}
