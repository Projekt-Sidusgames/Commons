package net.crytec.libs.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Utility methods for working with {@link Location}.
 */
public class UtilLoc {

  private static final BlockFace[] axis = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
  private static final BlockFace[] radial = {BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST};


  public static String locToString(final Location location) {
    final StringBuilder sb = new StringBuilder();
    sb.append(location.getWorld().getUID().toString()).append("@").append(location.getX()).append("@").append(location.getY()).append("@").append(location.getZ())
        .append("@").append(location.getYaw()).append("@").append(location.getPitch());

    return sb.toString();
  }

  public static Location locFromString(final String locationString) {
    if (locationString.contains(";")) {
      final Location location = importOldFormat(locationString);
      if (location != null) {
        Bukkit.getLogger().info("- Imported old location format - ");
      }
      return importOldFormat(locationString);
    }

    final String[] data = locationString.split("@");

    final World world = Bukkit.getWorld(UUID.fromString(data[0]));
    if (world == null) {
      return null;
    }
    return new Location(world, Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
  }

  private static Location importOldFormat(final String input) {
    if (input == null || input.equals("null")) {
      return Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    final String toParse = input.replace("_", ".");
    final String[] data = toParse.split(";");

    final String world = data[0];
    final double x = Double.parseDouble(data[1]);
    final double y = Double.parseDouble(data[2]);
    final double z = Double.parseDouble(data[3]);
    final float pitch = Float.parseFloat(data[4]);
    final float yaw = Float.parseFloat(data[5]);

    if (Bukkit.getWorld(world) == null) {
      return null;
    }

    final Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

    return location;
  }

  /**
   * Convert the yaw value to a BlockFace direction.
   *
   * @param yaw
   * @param useSubCardinalDirections - Returns values like NORTH_EAST
   * @return -
   */
  public static BlockFace yawToFace(final float yaw, final boolean useSubCardinalDirections) {
    if (useSubCardinalDirections) {
      return radial[Math.round(yaw / 45f) & 0x7].getOppositeFace();
    }

    return axis[Math.round(yaw / 90f) & 0x3].getOppositeFace();
  }


  /**
   * Check if the chunk at the given location is loaded.
   *
   * @param location
   * @return true if the chunk is loaded.
   */
  public static boolean isChunkLoaded(final Location location) {
    final int chunkX = location.getBlockX() >> 4;
    final int chunkZ = location.getBlockZ() >> 4;
    return location.getWorld().isChunkLoaded(chunkX, chunkZ);
  }

  /**
   * Check if the player is in the radius of the given location without respecting the heigth of a player.
   *
   * @param player
   * @param loc
   * @param radius
   * @return -
   */
  public static boolean isInRadius(final Player player, final Location loc, final double radius) {
    final double x = loc.getX() - player.getLocation().getX();
    final double z = loc.getZ() - player.getLocation().getZ();
    final double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
    if (distance <= radius) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the player is in the radius of the given location.
   *
   * @param player
   * @param loc
   * @param radius
   * @return -
   */
  public static boolean isInRadius3D(final Player player, final Location loc, final double radius) {
    final double x = loc.getX() - player.getLocation().getX();
    final double y = loc.getY() - player.getLocation().getY();
    final double z = loc.getZ() - player.getLocation().getZ();
    final double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    if (distance <= radius) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check if the location midpoint is in the radius of the location
   *
   * @param midpoint
   * @param loc
   * @param radius
   * @return -
   */
  public static boolean isInRadius3D(final Location midpoint, final Location loc, final double radius) {
    final double x = loc.getX() - midpoint.getX();
    final double y = loc.getY() - midpoint.getY();
    final double z = loc.getZ() - midpoint.getZ();
    final double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    if (distance <= radius) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Get a random location around the player
   *
   * @param player
   * @param XmaxDistance
   * @param ZmaxDistance
   * @return -
   */
  public static Location getRandomLocationArroundPlayer(final Player player, final int XmaxDistance, final int ZmaxDistance) {
    final World world = player.getWorld();

    int randomX = 0;
    int randomZ = 0;

    double x = player.getLocation().getX();
    double y = 0.0D;
    double z = player.getLocation().getZ();

    randomX = 1 + (int) (Math.random() * (XmaxDistance - 1 + 1));
    randomZ = 1 + (int) (Math.random() * (ZmaxDistance - 1 + 1));

    x = x + randomX;
    y = player.getLocation().clone().add(0, 1, 0).getY();
    z = z + randomZ;
    return new Location(world, x, y, z);
  }

  /**
   * Check if the given location is save.
   * <p>
   * If the given location is occupied or filled with fluid blocks, it will return false.
   *
   * @param loc
   * @return -
   */
  public static boolean isSafe(final Location loc) {

    final World world = loc.getWorld();
    final Block teleblock = world.getHighestBlockAt(loc).getRelative(BlockFace.DOWN);
    final Block block1 = world.getBlockAt(loc.add(0, 1, 0));
    final Block block2 = world.getBlockAt(loc.add(0, 2, 0));

    if (block1.isLiquid() || block2.isLiquid() || teleblock.isLiquid()) {
      return false;
    }
    if (!teleblock.getType().isSolid() && block1.getType() == Material.AIR && block2.getType() == Material.AIR) {
      return false;
    }

    return true;
  }

  /**
   * Get a random Location from the given world that's inside the worldborder
   *
   * @param w
   * @return -
   */
  public static Location getRandomLoc(final World w) {
    final int centerX = (int) w.getWorldBorder().getCenter().getX();
    final int centerZ = (int) w.getWorldBorder().getCenter().getZ();

    final int maxX = centerX + (int) w.getWorldBorder().getSize() / 2;
    final int maxZ = centerZ + (int) w.getWorldBorder().getSize() / 2;

    final int minX = centerX - (int) w.getWorldBorder().getSize() / 2;
    final int minZ = centerZ - (int) w.getWorldBorder().getSize() / 2;

    final int randomX = UtilMath.getRandom(minX, maxX);
    final int randomZ = UtilMath.getRandom(minZ, maxZ);

    return new Location(w, randomX, 0, randomZ);
  }

  public static String getLogString(final Location loc) {
    return "(" + loc.getWorld().getName() + ")" + " X: " + UtilMath.trim(1, loc.getX()) + " Y: " + UtilMath.trim(1, loc.getY()) + " Z: " + UtilMath.trim(1, loc.getZ());
  }

  public static List<Player> getClosestPlayersFromLocation(final Location location, final double distance) {
    final List<Player> result = new ArrayList<>();
    final double d2 = distance * distance;
    for (final Player player : location.getWorld().getPlayers()) {
      if (player.getLocation().add(0.0, 0.85, 0.0).distanceSquared(location) <= d2) {
        result.add(player);
      }
    }
    return result;
  }

  public static List<Entity> getClosestEntitiesFromLocation(final Location location, final double radius) {
    final double chunkRadius = (radius < 16.0) ? 1.0 : ((radius - radius % 16.0) / 16.0);
    final List<Entity> radiusEntities = new ArrayList<>();
    for (double chX = 0.0 - chunkRadius; chX <= chunkRadius; ++chX) {
      for (double chZ = 0.0 - chunkRadius; chZ <= chunkRadius; ++chZ) {
        final int x = (int) location.getX();
        final int y = (int) location.getY();
        final int z = (int) location.getZ();
        for (final Entity e : new Location(location.getWorld(), x + chX * 16.0, (double) y, z + chZ * 16.0).getChunk().getEntities()) {
          if (e.getLocation().distance(location) <= radius && e.getLocation().getBlock() != location.getBlock() && e instanceof Entity) {
            radiusEntities.add(e);
          }
        }
      }
    }
    return radiusEntities;
  }

  /**
   * Return the center of a given location/block
   *
   * @param loc
   * @return -
   */
  public static Location getCenter(final Location loc) {
    return loc.clone().add(0.5, 0, 0.5);
  }

  /**
   * A number of points in a circle (horizontal)
   *
   * @param center
   * @param radius
   * @param amount
   * @return -
   */
  public static ArrayList<Location> getCircle(final Location center, final double radius, final int amount) {
    final World world = center.getWorld();
    final double increment = (2 * Math.PI) / amount;
    final ArrayList<Location> locations = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      final double angle = i * increment;
      final double x = center.getX() + (radius * Math.cos(angle));
      final double z = center.getZ() + (radius * Math.sin(angle));
      locations.add(new Location(world, x, center.getY(), z));
    }
    return locations;
  }

  /**
   * Returns a target-oriented location (yaw / pitch) with a precision factor
   *
   * @param from
   * @param to
   * @param aiming 100 = 100% precision
   * @return -
   */
  public static Location lookAt(Location from, final Location to, final float aiming) {
    // Clone the loc to prevent applied changes to the input loc
    from = from.clone();

    // Values of change in distance (make it relative)
    final double dx = to.getX() - from.getX();
    final double dy = to.getY() - from.getY();
    final double dz = to.getZ() - from.getZ();

    // Set yaw
    if (dx != 0) {
      // Set yaw start value based on dx
      if (dx < 0) {
        from.setYaw((float) (1.5 * Math.PI));
      } else {
        from.setYaw((float) (0.5 * Math.PI));
      }
      from.setYaw((float) from.getYaw() - (float) Math.atan(dz / dx));
    } else if (dz < 0) {
      from.setYaw((float) Math.PI);
    }

    // Get the distance from dx/dz
    final double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

    // Set pitch
    from.setPitch((float) -Math.atan(dy / dxz));

    // Yaw
    float yawAiming = 360 - (aiming * 360 / 100);
    if (yawAiming > 0) {
      yawAiming = UtilMath.getRandom((int) yawAiming, (int) -yawAiming);
    }
    // Pitch
    float pitchAiming = 180 - (aiming * 180 / 100);
    if (pitchAiming > 0) {
      pitchAiming = UtilMath.getRandom((int) pitchAiming, (int) -pitchAiming);
    }

    // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
    from.setYaw(-from.getYaw() * 180f / (float) Math.PI + yawAiming);
    from.setPitch(from.getPitch() * 180f / (float) Math.PI + pitchAiming);
    return from;
  }
}
