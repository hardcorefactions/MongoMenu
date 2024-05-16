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
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import wtf.jsexp.mongomenu.MongoMenu;
import wtf.jsexp.mongomenu.utils.CC;
import wtf.jsexp.mongomenu.utils.Config;

@CommandAlias("mongomenu|menuplugin|mplugin")
@Description("Credits for MongoMenu")
public class MongoMenuCommand extends BaseCommand {
    @Default
    public void onMain(CommandSender s) {
        s.sendMessage(CC.t("&7&m------------------------"));
        s.sendMessage(CC.t("&a&lMongoMenu"));
        s.sendMessage(CC.t("&f"));
        s.sendMessage(CC.t("&2Author&7: &fjsexp &7(www.jsexp.wtf)"));
        s.sendMessage(CC.t("&2Version&7: &f" + MongoMenu.inst.getDescription().getVersion()));
        s.sendMessage(CC.t("&7&m------------------------"));
    }

    @Subcommand("reload")
    @CommandPermission("menus.reload")
    public void onReload(CommandSender s) {
        Config.reload();
        s.sendMessage(CC.t("&aPlugin reloaded successfully."));
    }
}
