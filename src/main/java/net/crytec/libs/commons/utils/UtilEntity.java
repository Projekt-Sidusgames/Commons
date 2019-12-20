package net.crytec.libs.commons.utils;

import java.util.Optional;
import net.crytec.libs.commons.utils.lang.EnumUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class UtilEntity {

  public static Material getSpawnEgg(final EntityType type) {
    if (!EnumUtils.isValidEnum(Material.class, type.toString() + "_SPAWN_EGG")) {
      return Material.BARRIER;
    }
    return Material.valueOf(type.toString() + "_SPAWN_EGG");
  }

  public static boolean isInRadius(final Entity entity, final Location loc, final double radius) {
    final double x = loc.getX() - entity.getLocation().getX();
    final double y = loc.getY() - entity.getLocation().getY();
    final double z = loc.getZ() - entity.getLocation().getZ();
    final double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

    return (distance <= radius);
  }

  /**
   * Returns the {@link SpawnReason} for the given entity.
   *
   * @param entity
   * @return Returns the {@link SpawnReason} for this entity. If no reason was found, CUSTOM will be returned
   */
  public static SpawnReason getSpawnReason(final Entity entity) {
    final Optional<String> reason = entity.getScoreboardTags().stream().filter(tag -> tag.startsWith("ct:corespawnreason;")).findFirst();
    return reason.map(s -> SpawnReason.valueOf(s.replace("ctcore:spawnreason;", ""))).orElse(SpawnReason.CUSTOM);
  }
}
