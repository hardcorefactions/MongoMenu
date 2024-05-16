/*
 * Made by jsexp
 * My websites:
 *  - https://jsexp.wtf (main)
 *  - https://hcteams.net (link to my github)
 *
 * MongoMenu © 2024
 */

package wtf.jsexp.mongomenu.menu;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MenuManager {
    private final Map<String, Menu> menus = new HashMap<>();

    public Menu getMenu(String name) {
        return menus.get(name);
    }
}
