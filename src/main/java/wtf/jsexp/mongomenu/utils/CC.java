package wtf.jsexp.mongomenu.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import wtf.jsexp.mongomenu.MongoMenu;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.broadcastMessage;
import static org.bukkit.Bukkit.getConsoleSender;

public class CC {
    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String t(String s) {
        return translate(s);
    }

    public static String[] translate(String[] array) {
        for(int i = 0; i < array.length; ++i) {
            array[i] = translate(array[i]);
        }

        return array;
    }

    public static List<String> translate(List<String> list) {
        return list.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static String toReadable(Enum<?> enu) {
        return WordUtils.capitalize(enu.name().replace("_", " ").toLowerCase());
    }

    public static void log(String s) {
        getConsoleSender().sendMessage("[" + MongoMenu.inst.getName() + "] " + translate(s).trim());
    }

    public static void broadcast(String s) {
        broadcastMessage(translate(s));
    }
}
