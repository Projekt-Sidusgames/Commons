package net.crytec.libs.commons.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UtilVec {

  public static TreeSet<String> sortKey(final Set<String> toSort) {
    final TreeSet<String> sortedSet = new TreeSet<>();
    for (final String cur : toSort) {
      sortedSet.add(cur);
    }
    return sortedSet;
  }

  public static Vector getTrajectory(final Entity from, final Entity to) {
    return getTrajectory(from.getLocation().toVector(), to.getLocation().toVector());
  }

  public static Vector getTrajectory(final Location from, final Location to) {
    return getTrajectory(from.toVector(), to.toVector());
  }

  public static Vector getTrajectory(final Vector from, final Vector to) {
    return to.subtract(from).normalize();
  }

  public static Vector getTrajectory2d(final Entity from, final Entity to) {
    return getTrajectory2d(from.getLocation().toVector(), to.getLocation().toVector());
  }

  public static Vector getTrajectory2d(final Location from, final Location to) {
    return getTrajectory2d(from.toVector(), to.toVector());
  }

  public static Vector getTrajectory2d(final Vector from, final Vector to) {
    return to.subtract(from).setY(0).normalize();
  }

  public static boolean HasSight(final Location from, final Player to) {
    return (HasSight(from, to.getLocation())) || (HasSight(from, to.getEyeLocation()));
  }

  public static boolean HasSight(final Location from, final Location to) {
    final Location cur = new Location(from.getWorld(), from.getX(), from.getY(), from.getZ());

    final double rate = 0.1D;
    final Vector vec = getTrajectory(from, to).multiply(0.1D);

    while (UtilMath.offset(cur, to) > rate) {
      cur.add(vec);

      if (cur.getBlock().getType().isSolid()) {
        return false;
      }
    }
    return true;
  }

  public static Vector cross(final Vector a, final Vector b) {
    final double x = a.getY() * b.getZ() - a.getZ() * b.getY();
    final double y = a.getZ() * b.getX() - a.getX() * b.getZ();
    final double z = a.getX() * b.getY() - a.getY() * b.getX();

    return new Vector(x, y, z).normalize();
  }

  public static Vector getRight(final Vector vec) {
    return cross(vec.clone().normalize(), new Vector(0, 1, 0));
  }

  public static Vector getLeft(final Vector vec) {
    return getRight(vec).multiply(-1);
  }

  public static Vector getBehind(final Vector vec) {
    return vec.clone().multiply(-1);
  }

  public static Vector getUp(final Vector vec) {
    return getDown(vec).multiply(-1);
  }

  public static Vector getDown(final Vector vec) {
    return cross(vec, getRight(vec));
  }

  public static float GetPitch(final Vector vec) {
    final double x = vec.getX();
    final double y = vec.getY();
    final double z = vec.getZ();
    final double xz = Math.sqrt(x * x + z * z);

    double pitch = Math.toDegrees(Math.atan(xz / y));
    if (y <= 0.0D) {
      pitch += 90.0D;
    } else {
      pitch -= 90.0D;
    }
    return (float) pitch;
  }

  public static float GetYaw(final Vector vec) {
    final double x = vec.getX();
    final double z = vec.getZ();

    double yaw = Math.toDegrees(Math.atan(-x / z));
    if (z < 0.0D) {
      yaw += 180.0D;
    }
    return (float) yaw;
  }

  public static Vector Normalize(final Vector vec) {
    if (vec.length() > 0.0D) {
      vec.normalize();
    }
    return vec;
  }

  public static Vector Clone(final Vector vec) {
    return new Vector(vec.getX(), vec.getY(), vec.getZ());
  }

  public static <T> T Random(final List<T> list) {
    return list.get(UtilMath.r(list.size()));
  }

  public static Vector bumpEntity(final Entity entity, final double power) {
    final Vector bump = entity.getLocation().toVector().add(new Vector(0.0D, 1.0D, 0.0D));
    bump.multiply(power);
    return bump;
  }

  public static Vector getPushVector(final Entity entity, final Location from, final double power) {
    final Vector bump = entity.getLocation().toVector().subtract(from.toVector()).normalize();
    bump.multiply(power);
    return bump;
  }

  public static Vector getPullVector(final Entity entity, final Location to, final double power) {
    final Vector pull = to.toVector().subtract(entity.getLocation().toVector()).normalize();
    pull.multiply(power);
    return pull;
  }

  public static void pushEntity(final Entity entity, final Location from, final double power) {
    entity.setVelocity(getPushVector(entity, from, power));
  }

  public static void pushEntity(final Entity entity, final Location from, final double power, final double fixedY) {
    final Vector vector = getPushVector(entity, from, power);
    vector.setY(fixedY);
    entity.setVelocity(vector);
  }

  public static void pullEntity(final Entity entity, final Location to, final double power) {
    entity.setVelocity(getPullVector(entity, to, power));
  }

  public static void pullEntity(final Entity entity, final Location from, final double power, final double fixedY) {
    final Vector vector = getPullVector(entity, from, power);
    vector.setY(fixedY);
    entity.setVelocity(vector);
  }

  public static void velocity(final Entity ent, final double str, final double yAdd, final double yMax, final boolean groundBoost) {
    velocity(ent, ent.getLocation().getDirection(), str, false, 0.0D, yAdd, yMax, groundBoost);
  }

  public static void velocity(final Entity ent, final Vector vec, final double str, final boolean ySet, final double yBase, final double yAdd, final double yMax, final boolean groundBoost) {
    if ((Double.isNaN(vec.getX())) || (Double.isNaN(vec.getY())) || (Double.isNaN(vec.getZ())) || (vec.length() == 0.0D)) {
      return;
    }

    if (ySet) {
      vec.setY(yBase);
    }

    vec.normalize();
    vec.multiply(str);

    vec.setY(vec.getY() + yAdd);

    if (vec.getY() > yMax) {
      vec.setY(yMax);
    }
    if ((groundBoost) && (ent.isOnGround())) {
      vec.setY(vec.getY() + 0.2D);
    }

    ent.setFallDistance(0.0F);
    ent.setVelocity(vec);
  }

  public static Location getLocationAwayFromPlayers(final ArrayList<Location> locs, final ArrayList<Player> players) {
    Location bestLoc = null;
    double bestDist = 0;

    for (final Location loc : locs) {
      double closest = -1;

      for (final Player player : players) {
        // Different Worlds
        if (!player.getWorld().equals(loc.getWorld())) {
          continue;
        }

        final double dist = UtilMath.offsetSquared(player.getLocation(), loc);

        if (closest == -1 || dist < closest) {
          closest = dist;
        }
      }

      if (closest == -1) {
        continue;
      }

      if (bestLoc == null || closest > bestDist) {
        bestLoc = loc;
        bestDist = closest;
      }
    }

    return bestLoc;
  }

  public static Location getLocationNearPlayers(final ArrayList<Location> locs, final ArrayList<Player> players, final ArrayList<Player> dontOverlap) {
    Location bestLoc = null;
    double bestDist = 0;

    for (final Location loc : locs) {
      double closest = -1;

      boolean valid = true;

      // Dont spawn on other players
      for (final Player player : dontOverlap) {
        if (!player.getWorld().equals(loc.getWorld())) {
          continue;
        }

        final double dist = UtilMath.offsetSquared(player.getLocation(), loc);

        if (dist < 0.8) {
          valid = false;
          break;
        }
      }

      if (!valid) {
        continue;
      }

      // Find closest player
      for (final Player player : players) {
        if (!player.getWorld().equals(loc.getWorld())) {
          continue;
        }

        final double dist = UtilMath.offsetSquared(player.getLocation(), loc);

        if (closest == -1 || dist < closest) {
          closest = dist;
        }
      }

      if (closest == -1) {
        continue;
      }

      if (bestLoc == null || closest < bestDist) {
        bestLoc = loc;
        bestDist = closest;
      }
    }

    return bestLoc;
  }
}