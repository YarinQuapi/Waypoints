package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.helpers.FileUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MessagesUtils {
    private static final Map<String, String> messages = new HashMap<>();
    //static Pattern urlPattern = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");

    private static FileConfiguration messagesData;



    public MessagesUtils() {
        File messagesFile = new File(Waypoints.getInstance().getDataFolder(), "messages.yml");
        messagesData = YamlConfiguration.loadConfiguration(messagesFile);

        FileUtils.registerData(messagesFile, messagesData);

        messagesData.getKeys(false).forEach(key -> messages.put(key, messagesData.getString(key)));
    }



    public static void reload() {
        messages.clear();

        File messagesFile = new File(Waypoints.getInstance().getDataFolder(), "messages.yml");
        messagesData = YamlConfiguration.loadConfiguration(messagesFile);

        FileUtils.registerData(messagesFile, messagesData);

        messagesData.getKeys(false).forEach(key -> messages.put(key, messagesData.getString(key)));
    }

    private static final Pattern pattern = Pattern.compile("\\b[{\\d}](?=})\\b");

    public static String getMessage(String key, Object... args) {
        if (key.contains(".")) {
            return getMessageFromData(key, args);
        }

        return MessageFormat.format(messages.get(key), args).replaceAll("&", "§");
        //return messages.get(key).formatted(args).replaceAll("&", "§");
    }

    public static String getMessageFromData(String key, Object... args) {
        return MessageFormat.format(messagesData.getString(key, key), args).replaceAll("&", "§");
        //return messagesData.getString(key, key).formatted(args).replaceAll("&", "§");
    }



    public static String getMessageLines(String key, Object... args) {
        StringBuilder message = new StringBuilder();

        for (String string : messagesData.getStringList(key)) {
            message.append(ChatColor.translateAlternateColorCodes('&', string + "\n"));
        }

        return message.toString().formatted(args);
    }



    public static String getRawFormattedString(String key, Object... args) {
        return messages.getOrDefault(key, key).replaceAll("&", "§").formatted(args);
    }



    public static String getRawString(String key) {
        return messages.getOrDefault(key, key).replaceAll("&", "§");
    }



    public static int getInt(String key) {
        return messagesData.getInt(key);
    }
}
