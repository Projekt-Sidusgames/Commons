package net.crytec.libs.commons.utils;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UtilPlayer {

  private static final Vector UP_VEC = new Vector(0, 1, 0);
  private static final Vector DOWN_VEC = new Vector(0, -1, 0);

  public static void playSound(final Player player, final Sound sound, final SoundCategory category) {
    player.playSound(player.getLocation(), sound, category, 1, 1);
  }

  public static BlockFace getExactFacing(final Player player) {
    final Vector direction = player.getEyeLocation().getDirection();
    if (direction.angle(UP_VEC) <= (Math.PI / 4)) {
      return BlockFace.UP;
    } else if (direction.angle(DOWN_VEC) <= (Math.PI / 4)) {
      return BlockFace.DOWN;
    }
    return player.getFacing();
  }

  public static void playSound(final Player player, final Sound sound) {
    playSound(player, sound, SoundCategory.MASTER, 1, 1);
  }

  public static void playSound(final Player player, final Sound sound, final float pitch) {
    playSound(player, sound, SoundCategory.MASTER, 1, pitch);
  }

  public static void playSound(final Player player, final Sound sound, final float volume, final float pitch) {
    player.playSound(player.getLocation(), sound, SoundCategory.MASTER, volume, pitch);
  }

  public static void playSound(final Player player, final Sound sound, final SoundCategory category, final float volume, final float pitch) {
    player.playSound(player.getLocation(), sound, category, volume, pitch);
  }

}
