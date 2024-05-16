/*
 * Made by jsexp
 * My websites:
 *  - https://jsexp.wtf (main)
 *  - https://hcteams.net (link to my github)
 *
 * MongoMenu © 2024
 */
package wtf.jsexp.mongomenu;

import co.aikar.commands.BukkitCommandManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.mrmicky.fastinv.FastInvManager;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.jsexp.mongomenu.commands.MenuCommand;
import wtf.jsexp.mongomenu.commands.MongoMenuCommand;
import wtf.jsexp.mongomenu.menu.Menu;
import wtf.jsexp.mongomenu.menu.MenuManager;
import wtf.jsexp.mongomenu.utils.CC;
import wtf.jsexp.mongomenu.utils.Config;

@Getter
public final class MongoMenu extends JavaPlugin {

    public static MongoMenu inst;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> menusCollection;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        inst = this;
        Config.check();

        CC.log("&7&m------------------------");
        CC.log("&2&lMongoMenu");
        CC.log("&f");
        CC.log("&2Author&7: &fjsexp &7(www.jsexp.wtf)");
        CC.log("&aPlugin has initialized successfully!");
        CC.log("&7&m------------------------");

        FastInvManager.register(this);
        loadCommands();
        loadDatabase();
    }

    private void loadDatabase()
    {
        MongoClientURI clientURI = new MongoClientURI(Config.database.getString("MONGO.URI"));
        MongoClient mongoClient = new MongoClient(clientURI);
        mongoDatabase = mongoClient.getDatabase(Config.database.getString("MONGO.NAME"));

        menusCollection = mongoDatabase.getCollection("menus");
        menuManager = new MenuManager();

        long startTime = System.currentTimeMillis();
        for (Document object : menusCollection.find())
        {
            String menuName = object.getString("name");

            if (!menuManager.getMenus().containsKey(menuName))
            {
                menuManager.getMenus().put(menuName, new Menu(menuName));
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        CC.log("&aLoaded " + menuManager.getMenus().size() + " menu(s) in " + elapsedTime + "ms.");
    }

    private void loadCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new MongoMenuCommand());
        commandManager.registerCommand(new MenuCommand());
    }

    @Override
    public void onDisable() {

    }
}
