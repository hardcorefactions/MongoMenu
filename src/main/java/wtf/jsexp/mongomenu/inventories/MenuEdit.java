package wtf.jsexp.mongomenu.inventories;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.jsexp.mongomenu.MongoMenu;
import wtf.jsexp.mongomenu.menu.Menu;
import wtf.jsexp.mongomenu.utils.CC;
import wtf.jsexp.mongomenu.utils.ItemBuilder;

import java.util.Objects;

public class MenuEdit implements Listener {
    private final Inventory inventory;
    private final Menu menu;

    public MenuEdit(Player player, Menu menu) {
        this.inventory = Bukkit.createInventory(null, 27, CC.t("&9Editing " + menu.getDisplayName()));
        this.menu = menu;

        Document contents = menu.getContents();
        if (contents != null) {
            for (String key : contents.keySet()) {
                int slot = Integer.parseInt(key);
                ItemStack itemStack = ItemBuilder.fromDocument((Document) contents.get(key));
                inventory.setItem(slot, itemStack);
            }
        }

        Bukkit.getPluginManager().registerEvents(this, MongoMenu.inst);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!player.hasPermission("mongomenu.command.edit") ||
                !(Objects.equals(player.getOpenInventory().getTopInventory().getName(), inventory.getName()) &&
                        !Objects.equals(player.getOpenInventory().getTitle(), player.getInventory().getTitle())))
        {
            return;
        }

        ItemStack[] items = event.getInventory().getContents();
        Document contents = new Document();

        int slot = 0;
        for (ItemStack item : items) {
            if (item == null) {
                slot = slot + 1;
                continue;
            }

            Document itemDoc = new ItemBuilder(item).toDocument();
            contents.append(String.valueOf(slot), itemDoc);
            slot = slot + 1;
        }

        menu.setContents(contents);
        menu.save();
    }
}
