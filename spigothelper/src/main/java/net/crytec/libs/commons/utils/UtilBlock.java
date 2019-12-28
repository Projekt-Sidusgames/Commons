package net.crytec.libs.commons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class UtilBlock {


  /**
   * Return a map of Blocks in the given Radius on the given Location from bottom to top
   *
   * @param loc -> Location
   * @param dR  -> Radius
   * @return the map with the blocks and radius
   */
  public static HashMap<Block, Double> getInRadius(final Location loc, final double dR) {
    return getInRadius(loc, dR, 999.0D);
  }

  /**
   * Returns a Map of Blocks on the given Location in the given radius, respecting the given height limit.
   *
   * @param loc
   * @param dR
   * @param heightLimit
   * @return The map with the blocks in the given radius and their distance
   */
  public static HashMap<Block, Double> getInRadius(final Location loc, final double dR, final double heightLimit) {
    final HashMap<Block, Double> blockList = new HashMap<>();
    final int iR = (int) dR + 1;

    for (int x = -iR; x <= iR; x++) {
      for (int z = -iR; z <= iR; z++) {
        for (int y = -iR; y <= iR; y++) {
          if (Math.abs(y) <= heightLimit) {

            final Block curBlock = loc.getWorld().getBlockAt((int) (loc.getX() + x), (int) (loc.getY() + y), (int) (loc.getZ() + z));

            final double offset = UtilMath.offset(loc, curBlock.getLocation().add(0.5D, 0.5D, 0.5D));

            if (offset <= dR) {
              blockList.put(curBlock, 1.0D - offset / dR);
            }
          }
        }
      }
    }
    return blockList;
  }

  /**
   * Returns a Map of Blocks on the given Location in the given radius
   *
   * @param block
   * @param dR    -> Radius
   * @return The Map with the blocks and the distance
   */
  public static HashMap<Block, Double> getInRadius(final Block block, final double dR) {
    final HashMap<Block, Double> blockList = new HashMap<>();
    final int iR = (int) dR + 1;

    for (int x = -iR; x <= iR; x++) {
      for (int z = -iR; z <= iR; z++) {
        for (int y = -iR; y <= iR; y++) {
          final Block curBlock = block.getRelative(x, y, z);

          final double offset = UtilMath.offset(block.getLocation(), curBlock.getLocation());

          if (offset <= dR) {
            blockList.put(curBlock, 1.0D - offset / dR);
          }
        }
      }
    }
    return blockList;
  }

  /**
   * Returns the highest block on the given location x and z
   *
   * @param world
   * @param x
   * @param z
   * @return The highest block at the given location
   */
  public static Block getHighest(final World world, final int x, final int z) {
    return getHighest(world, x, z, null);
  }

  /**
   * Returns the highest block on the given location x and z while ignoring a set of Materials
   *
   * @param world
   * @param x
   * @param z
   * @param ignore
   * @return The highest block
   */
  public static Block getHighest(final World world, final int x, final int z, final HashSet<Material> ignore) {
    Block block = world.getHighestBlockAt(x, z);

    while ((block.getType().isSolid()) || (block.getType().toString().contains("LEAVES")) || ((ignore != null) && (ignore.contains(block.getType())))) {
      block = block.getRelative(BlockFace.DOWN);
    }
    return block.getRelative(BlockFace.UP);
  }

  /**
   * Returns the surrounding blocks
   *
   * @param block
   * @param diagonals
   * @return The surrounding blocks
   */
  public static ArrayList<Block> getSurrounding(final Block block, final boolean diagonals) {
    final ArrayList<Block> blocks = new ArrayList<>();

    if (diagonals) {
      for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
          for (int z = -1; z <= 1; z++) {
            if ((x != 0) || (y != 0) || (z != 0)) {

              blocks.add(block.getRelative(x, y, z));
            }
          }
        }
      }
    } else {
      blocks.add(block.getRelative(BlockFace.UP));
      blocks.add(block.getRelative(BlockFace.DOWN));
      blocks.add(block.getRelative(BlockFace.NORTH));
      blocks.add(block.getRelative(BlockFace.SOUTH));
      blocks.add(block.getRelative(BlockFace.EAST));
      blocks.add(block.getRelative(BlockFace.WEST));
    }

    return blocks;
  }

  /**
   * Returns true if the other block is visible (not surrounded by air)
   *
   * @param block
   * @return True if the block is visible
   */
  public static boolean isVisible(final Block block) {
    for (final Block other : getSurrounding(block, false)) {
      if (!other.getType().isOccluding()) {
        return true;
      }
    }
    return false;
  }
}
