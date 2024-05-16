/*
 * Made by jsexp
 * My websites:
 *  - https://jsexp.wtf (main)
 *  - https://hcteams.net (link to my github)
 *
 * MongoMenu © 2024
 */

package wtf.jsexp.mongomenu.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import wtf.jsexp.mongomenu.MongoMenu;

import java.io.File;

public class Config {
    static final File configFile = new File(MongoMenu.inst.getDataFolder(), "config.yml");
    static final File databaseFile = new File(MongoMenu.inst.getDataFolder(), "database.yml");

    public static FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
    public static FileConfiguration database = YamlConfiguration.loadConfiguration(databaseFile);

    public static void reload() {
        check();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void check() {
        if(!configFile.exists()) {
            MongoMenu.inst.saveResource("config.yml", false);
            config = YamlConfiguration.loadConfiguration(configFile);
        }
        if(!databaseFile.exists()) {
            MongoMenu.inst.saveResource("database.yml", false);
            database = YamlConfiguration.loadConfiguration(databaseFile);
        }
    }
}
