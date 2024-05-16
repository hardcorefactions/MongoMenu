package wtf.jsexp.mongomenu.utils.compatibility.effect;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import wtf.jsexp.mongomenu.utils.BukkitUtils;

public enum CompatibleEffect {
   HEART("HEART", "HEART_PARTICLE", "HEART_PARTICLE");

   private final String effect8;
   private final String effect912;
   private final String effect13;

   CompatibleEffect(String effect8, String effect912, String effect13) {
      this.effect8 = effect8;
      this.effect912 = effect912;
      this.effect13 = effect13;
   }

   CompatibleEffect(String effect8, String effect913) {
      this(effect8, effect913, effect913);
   }

   CompatibleEffect(String effect13) {
      this(null, null, effect13);
   }

   public void play(Player player) {
      if (this.getEffect() != null) {
         player.getWorld().playEffect(player.getLocation(), this.getEffect(), 0);
      }

   }

   private Effect getEffect() {
      if (BukkitUtils.SERVER_VERSION_INT <= 8) {
         return this.effect8 == null ? Effect.valueOf("HEART") : Effect.valueOf(this.effect8);
      } else if (BukkitUtils.SERVER_VERSION_INT > 9 && BukkitUtils.SERVER_VERSION_INT < 13) {
         return this.effect912 == null ? Effect.valueOf("HEART_PARTICLE") : Effect.valueOf(this.effect912);
      } else if (BukkitUtils.SERVER_VERSION_INT >= 13) {
         return this.effect13 == null ? Effect.valueOf("HEART_PARTICLE") : Effect.valueOf(this.effect13);
      } else {
         return null;
      }
   }
}
