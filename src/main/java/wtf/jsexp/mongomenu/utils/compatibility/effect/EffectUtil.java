package wtf.jsexp.mongomenu.utils.compatibility.effect;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import wtf.jsexp.mongomenu.utils.CC;

public final class EffectUtil {
   public static void play(Player player, String effect) {
      if (!effect.isEmpty()) {
         try {
            player.getWorld().playEffect(player.getLocation(), Effect.valueOf(effect), 1);
         } catch (Exception var3) {
            CC.log("&cEffect '" + effect + "' is not valid, please use a valid effect.");
         }
      }

   }

   private EffectUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
