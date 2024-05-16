package wtf.jsexp.mongomenu.inventories;

import fr.mrmicky.fastinv.FastInv;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import wtf.jsexp.mongomenu.menu.Menu;
import wtf.jsexp.mongomenu.utils.CC;
import wtf.jsexp.mongomenu.utils.ItemBuilder;

public class MenuPreview extends FastInv {

    public MenuPreview(Menu menu) {
        super(3 * 9, CC.t(menu.getDisplayName()));
        Document contents = menu.getContents();

        if (contents != null) {
            for (String key : contents.keySet()) {
                int slot = Integer.parseInt(key);
                ItemStack itemStack = ItemBuilder.fromDocument((Document) contents.get(key));
                setItem(slot, itemStack);
            }
        }
    }

}