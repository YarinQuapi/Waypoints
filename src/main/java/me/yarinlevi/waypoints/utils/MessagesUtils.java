package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.FileUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.FileUtil;

import java.io.File;
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

    public static String getMessage(String key, Object... args) {
        return Constants.PREFIX + messages.get(key).replaceAll("&", "§").formatted(args);
    }

    public static String getMessageLines(String key, Object... args) {
        StringBuilder message = new StringBuilder();

        for (String string : messagesData.getStringList(key)) {
            message.append(string.replaceAll("&", "§"));
        }

        return message.toString().formatted(args);
    }

    public static String getRawFormattedString(String key, Object... args) {
        return messages.getOrDefault(key, key).replaceAll("&", "§").formatted(args);
    }

    public static String getRawString(String key) {
        return messages.getOrDefault(key, key).replaceAll("&", "§");
    }
}
