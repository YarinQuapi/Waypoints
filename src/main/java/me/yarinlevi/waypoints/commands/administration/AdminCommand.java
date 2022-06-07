package me.yarinlevi.waypoints.commands.administration;

import me.yarinlevi.waypoints.commands.SubCommand;
import me.yarinlevi.waypoints.commands.administration.sub.CheckSubCommand;
import me.yarinlevi.waypoints.commands.administration.sub.ChunkScanSubCommand;
import me.yarinlevi.waypoints.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YarinQuapi
 **/
public class AdminCommand implements CommandExecutor {
    private final Map<String, SubCommand> commandMap = new HashMap<>();

    public AdminCommand() {
        commandMap.put("check", new CheckSubCommand());
        commandMap.put("chunkscan", new ChunkScanSubCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (commandMap.containsKey(args[0].toLowerCase())) {
                commandMap.get(args[0].toLowerCase()).run(player, args);
                return true;
            } else {
                sender.sendMessage("Sorry, couldn't find the command.");
                return false;
            }
        }

        sender.sendMessage(MessagesUtils.getMessage("must_be_player"));
        return false;
    }
}
