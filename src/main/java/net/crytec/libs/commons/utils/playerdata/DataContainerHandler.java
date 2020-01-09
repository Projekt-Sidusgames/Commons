package net.crytec.libs.commons.utils.playerdata;

import com.google.common.collect.Maps;
import java.util.HashMap;
import net.crytec.libs.commons.utils.CommonsAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class DataContainerHandler {

  static {
    host = CommonsAPI.getHost();
  }

  private static final JavaPlugin host;
  private final PersistentDataHolder holder;
  private final PersistentDataContainer container;
  private final HashMap<String, NamespacedKey> keyCache = Maps.newHashMap();

  public DataContainerHandler(final PersistentDataHolder holder) {
    this.holder = holder;
    container = holder.getPersistentDataContainer();
  }

  public boolean hasKey(final String key, final PersistentDataType type) {
    return container.has(toKey(key), type);
  }

  public void remove(final String key) {
    container.remove(toKey(key));
  }

  public void setString(final String key, final String value) {
    container.set(toKey(key), PersistentDataType.STRING, value);
  }

  public String getString(final String key) {
    return container.get(toKey(key), PersistentDataType.STRING);
  }

  public void setDouble(final String key, final double value) {
    container.set(toKey(key), PersistentDataType.DOUBLE, value);
  }

  public double getDouble(final String key) {
    return container.get(toKey(key), PersistentDataType.DOUBLE);
  }

  public void setInt(final String key, final int value) {
    container.set(toKey(key), PersistentDataType.INTEGER, value);
  }

  public Integer getInt(final String key) {
    return container.get(toKey(key), PersistentDataType.INTEGER);
  }

  public void setLong(final String key, final long value) {
    container.set(toKey(key), PersistentDataType.LONG, value);
  }

  public long getLong(final String key) {
    return container.get(toKey(key), PersistentDataType.LONG);
  }

  public void setBoolean(final String key, final boolean value) {
    container.set(toKey(key), PersistentDataType.BYTE, (value ? (byte) 0 : (byte) 1));
  }

  public boolean getBoolean(final String key) {
    final byte val = container.get(toKey(key), PersistentDataType.BYTE);
    return val == (byte) 1 ? true : false;
  }

  public void setContainer(final String key, final PersistentDataContainer value) {
    if (value.isEmpty()) {
      if (container.has(toKey(key), PersistentDataType.TAG_CONTAINER)) {
        container.remove(toKey(key));
      }
      return;
    }
    container.set(toKey(key), PersistentDataType.TAG_CONTAINER, value);
  }

  public PersistentDataContainer getContainer(final String key) {
    return container.get(toKey(key), PersistentDataType.TAG_CONTAINER);
  }


  private NamespacedKey toKey(final String key) {
    return keyCache.computeIfAbsent(key, s -> new NamespacedKey(host, key));
  }
}