package me.yarinlevi.waypoints.commands.administration;

import me.yarinlevi.waypoints.commands.administration.sub.CheckSubCommand;
import me.yarinlevi.waypoints.commands.administration.sub.ChunkScanSubCommand;
import me.yarinlevi.waypoints.commands.administration.sub.TeleportSubCommand;
import me.yarinlevi.waypoints.commands.shared.SubCommand;
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
    private final Map<String, String> aliases = new HashMap<>();


    public AdminCommand() {
        commandMap.put("check", new CheckSubCommand());
        commandMap.put("chunkscan", new ChunkScanSubCommand());
        commandMap.put("teleport", new TeleportSubCommand());

        commandMap.forEach((key, cmd) -> {
            if (cmd.getAliases() != null && !cmd.getAliases().isEmpty()) {
                cmd.getAliases().forEach(alias -> aliases.put(alias, key));
            }
        });
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player player) {
            SubCommand subCommand;

            if (commandMap.containsKey(args[0].toLowerCase())) {
                subCommand = commandMap.get(args[0].toLowerCase());
            } else if (aliases.containsKey(args[0].toLowerCase())) {
                subCommand = commandMap.get(aliases.get(args[0].toLowerCase()));
            } else {
                player.sendMessage("Sorry, couldn't find that command.");
                return false;
            }

            subCommand.run(player, args);
            return true;

        }

        sender.sendMessage(MessagesUtils.getMessage("must_be_player"));
        return false;
    }
}
