package me.yarinlevi.waypoints.utils;

import me.yarinlevi.waypoints.Waypoints;
import me.yarinlevi.waypoints.data.helpers.FileUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
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

    private static final Pattern pattern = Pattern.compile("([{])(?<=\\{)(\\d+)(?=})([}])");

    public static String getMessage(String key, Object... args) {
        return getCorrectString(key).formatted(args).replaceAll("&", "§");
    }

    public static String getMessageLines(String key, Object... args) {
        StringBuilder message = new StringBuilder();

        for (String string : messagesData.getStringList(key)) {
            message.append(ChatColor.translateAlternateColorCodes('&', string + "\n"));
        }

        return getCorrectFormat(message.toString()).formatted(args);
    }

    public static String getRawString(String key) {
        return messages.getOrDefault(key, key).replaceAll("&", "§");
    }

    public static int getInt(String key) {
        return messagesData.getInt(key);
    }

    private static String getCorrectFormat(String message) {
        Matcher matcher = pattern.matcher(message);

        return matcher.replaceAll("%" + "$2" + "\\$s");
    }

    private static String getCorrectString(String key) {
        String raw = getMessageRaw(key);

        return getCorrectFormat(raw);
    }

    private static String getMessageRaw(String key) {
        return key.contains(".") ? messagesData.getString(key) : messages.get(key);
    }
}
