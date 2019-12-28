package net.crytec.libs.commons.utils;

import com.google.common.base.Preconditions;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;

public class UtilChunk {

  public static long getChunkKey(final int x, final int z) {
    return (long) x & 0xffffffffL | ((long) z & 0xffffffffL) << 32;
  }

  public static long getChunkKey(final Chunk chunk) {
    return (long) chunk.getX() & 0xffffffffL | ((long) chunk.getZ() & 0xffffffffL) << 32;
  }

  public static Chunk keyToChunk(final World world, final long chunkID) {
    Preconditions.checkArgument(world != null, "World cannot be null");
    return world.getChunkAt((int) chunkID, (int) (chunkID >> 32));
  }

  public static boolean isChunkLoaded(final Location loc) {
    final int chunkX = loc.getBlockX() >> 4;
    final int chunkZ = loc.getBlockZ() >> 4;
    return loc.getWorld().isChunkLoaded(chunkX, chunkZ);
  }

  public static long getChunkKey(final Location loc) {
    return getChunkKey(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
  }

  public static long getChunkKey(final ChunkSnapshot chunk) {
    return (long) chunk.getX() & 0xffffffffL | ((long) chunk.getZ() & 0xffffffffL) << 32;
  }
}
