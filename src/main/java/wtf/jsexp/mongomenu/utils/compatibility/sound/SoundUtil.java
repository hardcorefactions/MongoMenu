package wtf.jsexp.mongomenu.utils.compatibility.sound;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import wtf.jsexp.mongomenu.utils.CC;

public final class SoundUtil {
   public static void play(Player player, String sound) {
      if (!sound.isEmpty()) {
         try {
            player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
         } catch (Exception var3) {
            CC.log("&cSound '" + sound + "' is not valid, please use a valid sound.");
         }
      }

   }

   private SoundUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
