package net.crytec.libs.commons.utils;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class UtilMath {

  public static final float nanoToSec = 1.0E-009F;
  public static final float FLOAT_ROUNDING_ERROR = 1.0E-006F;
  public static final float PI = 3.141593F;
  public static final float PI2 = 6.283186F;
  public static final float SQRT_3 = 1.73205F;
  public static final float E = 2.718282F;
  private static final int SIN_BITS = 14;
  private static final int SIN_MASK = 16383;
  private static final int SIN_COUNT = 16384;
  private static final float radFull = 6.283186F;
  private static final float degFull = 360.0F;
  private static final float radToIndex = 2607.5945F;
  private static final float degToIndex = 45.511112F;
  public static final float radiansToDegrees = 57.295776F;
  public static final float radDeg = 57.295776F;
  public static final float degreesToRadians = 0.01745329F;
  public static final float degRad = 0.01745329F;
  private static final int ATAN2_BITS = 7;
  private static final int ATAN2_BITS2 = 14;
  private static final int ATAN2_MASK = 16383;
  private static final int ATAN2_COUNT = 16384;
  public static Random random = new Random();
  private static final int BIG_ENOUGH_INT = 16384;
  private static final double BIG_ENOUGH_FLOOR = 16384.0D;
  private static final double CEIL = 0.9999999000000001D;
  private static final double BIG_ENOUGH_CEIL = 16384.999999999996D;
  private static final double BIG_ENOUGH_ROUND = 16384.5D;

  private static class Sin {

    static final float[] table = new float[16384];

    static {
      for (int i = 0; i < 16384; i++) {
        table[i] = ((float) Math.sin((i + 0.5F) / 16384.0F * 6.283186F));
      }
      for (int i = 0; i < 360; i += 90) {
        table[((int) (i * 45.511112F) & 0x3FFF)] = ((float) Math.sin(i * 0.01745329F));
      }
    }
  }

  public static float sin(final float radians) {
    return Sin.table[((int) (radians * 2607.5945F) & 0x3FFF)];
  }

  public static float cos(final float radians) {
    return Sin.table[((int) ((radians + 1.570796F) * 2607.5945F) & 0x3FFF)];
  }

  public static float sinDeg(final float degrees) {
    return Sin.table[((int) (degrees * 45.511112F) & 0x3FFF)];
  }

  public static float cosDeg(final float degrees) {
    return Sin.table[((int) ((degrees + 90.0F) * 45.511112F) & 0x3FFF)];
  }

  static final int ATAN2_DIM = (int) Math.sqrt(16384.0D);
  private static final float INV_ATAN2_DIM_MINUS_1 = 1.0F / (ATAN2_DIM - 1);

  private static class Atan2 {

    static final float[] table = new float[16384];

    static {
      for (int i = 0; i < ATAN2_DIM; i++) {
        for (int j = 0; j < ATAN2_DIM; j++) {
          final float x0 = i / ATAN2_DIM;
          final float y0 = j / ATAN2_DIM;
          table[(j * ATAN2_DIM + i)] = ((float) Math.atan2(y0, x0));
        }
      }
    }
  }

  public static float atan2(float y, float x) {
    final float add;
    final float mul;
    if (x < 0.0F) {
      if (y < 0.0F) {
        y = -y;
        mul = 1.0F;
      } else {
        mul = -1.0F;
      }
      x = -x;
      add = -3.141593F;
    } else {
      if (y < 0.0F) {
        y = -y;
        mul = -1.0F;
      } else {
        mul = 1.0F;
      }
      add = 0.0F;
    }
    final float invDiv = 1.0F / ((Math.max(x, y)) * INV_ATAN2_DIM_MINUS_1);
    if (invDiv == (1.0F / 1.0F)) {
      return ((float) Math.atan2(y, x) + add) * mul;
    }
    final int xi = (int) (x * invDiv);
    final int yi = (int) (y * invDiv);
    return (Atan2.table[(yi * ATAN2_DIM + xi)] + add) * mul;
  }

  public static int random(final int range) {
    return random.nextInt(range + 1);
  }

  public static int random(final int start, final int end) {
    return start + random.nextInt(end - start + 1);
  }

  public static boolean randomBoolean() {
    return random.nextBoolean();
  }

  public static boolean randomBoolean(final float chance) {
    return random() < chance;
  }

  public static float random() {
    return random.nextFloat();
  }

  public static float random(final float range) {
    return random.nextFloat() * range;
  }

  public static float random(final float start, final float end) {
    return start + random.nextFloat() * (end - start);
  }

  public static int nextPowerOfTwo(int value) {
    if (value == 0) {
      return 1;
    }
    value--;
    value |= value >> 1;
    value |= value >> 2;
    value |= value >> 4;
    value |= value >> 8;
    value |= value >> 16;
    return value + 1;
  }

  public static boolean isPowerOfTwo(final int value) {
    return (value != 0) && ((value & value - 1) == 0);
  }

  public static int clamp(final int value, final int min, final int max) {
    if (value < min) {
      return min;
    }
    if (value > max) {
      return max;
    }
    return value;
  }

  public static short clamp(final short value, final short min, final short max) {
    if (value < min) {
      return min;
    }
    if (value > max) {
      return max;
    }
    return value;
  }

  public static float clamp(final float value, final float min, final float max) {
    if (value < min) {
      return min;
    }
    if (value > max) {
      return max;
    }
    return value;
  }

  public static int floor(final float x) {
    return (int) (x + 16384.0D) - 16384;
  }

  public static int floorPositive(final float x) {
    return (int) x;
  }

  public static int ceil(final float x) {
    return (int) (x + 16384.999999999996D) - 16384;
  }

  public static int ceilPositive(final float x) {
    return (int) (x + 0.9999999000000001D);
  }

  public static int round(final float x) {
    return (int) (x + 16384.5D) - 16384;
  }

  public static double unsafeRound(final double number, final int points) {
    final double multi = Math.pow(10, points);
    return (((int) (number * multi)) / multi);
  }

  public static int roundPositive(final float x) {
    return (int) (x + 0.5F);
  }

  public static boolean isZero(final float value) {
    return Math.abs(value) <= 1.0E-006F;
  }

  public static boolean isZero(final float value, final float tolerance) {
    return Math.abs(value) <= tolerance;
  }

  public static boolean isEqual(final float a, final float b) {
    return Math.abs(a - b) <= 1.0E-006F;
  }

  public static boolean isEqual(final float a, final float b, final float tolerance) {
    return Math.abs(a - b) <= tolerance;
  }

  public static double trim(final int degree, final double d) {
    final StringBuilder format = new StringBuilder("#.#");

    for (int i = 1; i < degree; i++) {
      format.append("#");
    }
    final DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
    dfs.setDecimalSeparator('.');
    final DecimalFormat twoDForm = new DecimalFormat(format.toString(), dfs);
    return Double.parseDouble(twoDForm.format(d));
  }

  public static int r(final int i) {
    return random.nextInt(i);
  }

  public static double offset2d(final Entity a, final Entity b) {
    return offset2d(a.getLocation().toVector(), b.getLocation().toVector());
  }

  public static double offset2d(final Location a, final Location b) {
    return offset2d(a.toVector(), b.toVector());
  }

  public static double offset2d(final Vector a, final Vector b) {
    a.setY(0);
    b.setY(0);
    return a.subtract(b).length();
  }

  public static double offset(final Entity a, final Entity b) {
    return offset(a.getLocation().toVector(), b.getLocation().toVector());
  }

  public static double offset(final Location a, final Location b) {
    return offset(a.toVector(), b.toVector());
  }

  public static double offset(final Vector a, final Vector b) {
    return a.subtract(b).length();
  }

  public static double offsetSquared(final Entity a, final Entity b) {
    return offsetSquared(a.getLocation(), b.getLocation());
  }

  public static double offsetSquared(final Location a, final Location b) {
    return offsetSquared(a.toVector(), b.toVector());
  }

  public static double offsetSquared(final Vector a, final Vector b) {
    return a.distanceSquared(b);
  }

  public static final Vector rotateAroundAxisX(final Vector vector, final double d) {
    final double d2 = Math.cos(d);
    final double d3 = Math.sin(d);
    final double d4 = vector.getY() * d2 - vector.getZ() * d3;
    final double d5 = vector.getY() * d3 + vector.getZ() * d2;
    return vector.setY(d4).setZ(d5);
  }

  public static final Vector rotateAroundAxisY(final Vector vector, final double d) {
    final double d2 = Math.cos(d);
    final double d3 = Math.sin(d);
    final double d4 = vector.getX() * d2 + vector.getZ() * d3;
    final double d5 = vector.getX() * (-d3) + vector.getZ() * d2;
    return vector.setX(d4).setZ(d5);
  }

  public static final Vector rotateAroundAxisZ(final Vector vector, final double d) {
    final double d2 = Math.cos(d);
    final double d3 = Math.sin(d);
    final double d4 = vector.getX() * d2 - vector.getY() * d3;
    final double d5 = vector.getX() * d3 + vector.getY() * d2;
    return vector.setX(d4).setY(d5);
  }

  public static final Vector rotateVector(final Vector vector, final double d, final double d2, final double d3) {
    UtilMath.rotateAroundAxisX(vector, d);
    UtilMath.rotateAroundAxisY(vector, d2);
    UtilMath.rotateAroundAxisZ(vector, d3);
    return vector;
  }

  public static Vector rotate(Vector vector, final Location location) {
    final double d = (double) (location.getYaw() / 180.0f) * 3.141592653589793;
    final double d2 = (double) (location.getPitch() / 180.0f) * 3.141592653589793;
    vector = UtilMath.rotateAroundAxisX(vector, d2);
    vector = UtilMath.rotateAroundAxisY(vector, -d);
    return vector;
  }

  public static float randomRange(final float f, final float f2) {
    return f + (float) Math.random() * (f2 - f);
  }

  public static int randomRange(final int n, final int n2) {
    final Random random = new Random();
    return random.nextInt(n2 - n + 1) + n;
  }

  public static int randomInt(final Range<Integer> range) {
    final ContiguousSet<Integer> set = ContiguousSet.create(range, DiscreteDomain.integers());
    return set.asList().get(new Random().nextInt(set.size()));
  }

  public static double randomRange(final double d, final double d2) {
    return Math.random() < 0.5 ? (1.0 - Math.random()) * (d2 - d) + d : Math.random() * (d2 - d) + d;
  }

  public static int getRandomWithExclusion(final int n, final int n2, final int... arrn) {
    int n3 = n + random.nextInt(n2 - n + 1 - arrn.length);
    final int n4 = arrn.length;
    int n5 = 0;
    while (n5 < n4) {
      final int n6 = arrn[n5];
      if (n3 < n6) {
        break;
      }
      ++n3;
      ++n5;
    }
    return n3;
  }

  public static boolean isDouble(final String amount) {
    try {
      Double.parseDouble(amount);
      return true;
    } catch (final NumberFormatException e) {
      return false;
    }
  }

  public static Vector getRandomVector() {
    final double d = random.nextDouble() * 2.0 - 1.0;
    final double d2 = random.nextDouble() * 2.0 - 1.0;
    final double d3 = random.nextDouble() * 2.0 - 1.0;
    return new Vector(d, d2, d3).normalize();
  }

  public static boolean isInt(final String amount) {
    try {
      Integer.parseInt(amount);
      return true;
    } catch (final NumberFormatException e) {
      return false;
    }
  }

  public static boolean isInCooldown(final long lastUse, final int cooldownInSec) {
    final long secondsLeft = ((lastUse / 1000) + cooldownInSec) - (System.currentTimeMillis() / 1000);
    return secondsLeft > 0;
  }

  public static int getRandom(final int lower, final int upper) {
    return random.nextInt(upper - lower + 1) + lower;
  }

  public static boolean betweenExclusive(final int x, final int min, final int max) {
    return x > min && x < max;
  }

  public static boolean isDivisible(final int by, final int i) {
    return i % by == 0;
  }
}
