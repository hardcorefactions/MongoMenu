/*
 * Made by jsexp
 * My websites:
 *  - https://jsexp.wtf (main)
 *  - https://hcteams.net (link to my github)
 *
 * MongoMenu © 2024
 */

package wtf.jsexp.mongomenu.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import wtf.jsexp.mongomenu.MongoMenu;
import wtf.jsexp.mongomenu.inventories.MenuEdit;
import wtf.jsexp.mongomenu.inventories.MenuPreview;
import wtf.jsexp.mongomenu.menu.Menu;
import wtf.jsexp.mongomenu.utils.CC;

@CommandAlias("menu|menus")
@Description("Main command for menu")
public class MenuCommand extends BaseCommand {
    @HelpCommand
    @Private
    @CommandPermission("mongomenu.command.help")
    public void onHelp(CommandSender s, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create")
    @Description("Create a menu")
    @CommandPermission("mongomenu.command.create")
    @Syntax("<name>")
    public void onMenuCreate(Player player, String menuName) {
        if (MongoMenu.inst.getMenuManager().getMenu(menuName) != null) {
            player.sendMessage(CC.t("&cThat menu already exists!"));
            return;
        }

        Menu menu = new Menu(menuName);
        menu.setDisplayName("&9" + menuName);
        menu.save();

        MongoMenu.inst.getMenuManager().getMenus().put(menuName, menu);
        player.sendMessage(CC.t("&aMenu '" + menuName + "' successfully created!"));
    }

    @Subcommand("delete")
    @Description("Delete a menu")
    @CommandPermission("mongomenu.command.delete")
    @Syntax("<name>")
    public void onMenuDelete(Player player, String menuName) {
        if (MongoMenu.inst.getMenuManager().getMenu(menuName) == null) {
            player.sendMessage(CC.t("&cThat menu doesn't exist!"));
            return;
        }

        MongoMenu.inst.getMenuManager().getMenu(menuName).delete();
        player.sendMessage(CC.t("&cMenu '" + menuName + "' successfully deleted!"));
    }

    @Subcommand("list")
    @Description("Lists all menu")
    @CommandPermission("mongomenu.command.list")
    public void onMenuDelete(Player player) {
        for (Menu menu : MongoMenu.inst.getMenuManager().getMenus().values()) {
            player.sendMessage(CC.t(menu.getDisplayName()));
        }
    }

    @Subcommand("preview")
    @Description("Preview a menu")
    public void onMenuPreview(Player player, String menuName) {
        Menu menu = MongoMenu.inst.getMenuManager().getMenu(menuName);

        if (menu == null) {
            player.sendMessage(CC.t("&cThat menu doesn't exist!"));
            return;
        }

        new MenuPreview(menu).open(player);
    }

    @Subcommand("edit")
    @Description("Edit a menu")
    @CommandPermission("mongomenu.command.edit")
    public void onMenuEdit(Player player, String menuName) {
        Menu menu = MongoMenu.inst.getMenuManager().getMenu(menuName);

        if (menu == null) {
            player.sendMessage(CC.t("&cThat menu doesn't exist!"));
            return;
        }

        new MenuEdit(player, menu);
    }

    @Subcommand("setdisplay")
    @Description("Set the display of a menu")
    @CommandPermission("mongomenu.command.edit")
    public void onMenuSetDisplay(Player player, String menuName, String menuDisplay) {
        Menu menu = MongoMenu.inst.getMenuManager().getMenu(menuName);

        if (menu == null) {
            player.sendMessage(CC.t("&cThat menu doesn't exist!"));
            return;
        }

        menu.setDisplayName(menuDisplay);
        menu.save();
        player.sendMessage(CC.t("&aSuccessfully set the crate's display name."));
    }
}