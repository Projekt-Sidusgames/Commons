package net.crytec.libs.commons.utils;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.crytec.libs.commons.utils.lang.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Creates a new Plugin Configuration at the given path with the given name. This class does not throw any IOExceptions when saving/loading classes instead they just print warnings into the console.
 */
public class PluginConfig extends YamlConfiguration {

  private final File file;
  private final JavaPlugin plugin;

  public PluginConfig(final JavaPlugin plugin, final File path, final String configname) {
    this.plugin = plugin;
    final File configfilepath = new File(path, configname);
    if (!path.exists() && !path.mkdirs()) {
      plugin.getLogger().warning("Failed to create directory at " + path.getAbsolutePath());
    }
    if (!configfilepath.exists()) {
      try {
        if (plugin.getResource(configname) != null) {
          plugin.saveResource(configname, false);
          Files.move(new File(plugin.getDataFolder(), configname), configfilepath);
          plugin.getLogger().info("Created default configuration - " + configname);
        } else {
          configfilepath.createNewFile();
        }
      } catch (final IOException e) {
        plugin.getLogger().severe("Failed to create configuration file for " + configname + "!");
        plugin.getLogger().severe("Path: " + path.getAbsolutePath() + " Filename: " + configname);
        e.printStackTrace();
      }
    }
    file = configfilepath;
    try {
      load(file);
    } catch (final IOException | InvalidConfigurationException e) {
      Bukkit.getLogger().severe("Failed to deserialize configuration: " + e.getMessage());
    }
  }

  public void reloadConfig(final boolean save) {
    if (save) {
      saveConfig();
    }
    try {
      load(file);
    } catch (final IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the configuration file to the disk.
   */
  public void saveConfig() {
    try {
      save(file);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Updates the configuration file with the given path and default value if its not set.<p>
   * <p>
   * Note: Does <b>NOT</b> save the configuration.
   *
   * @param path
   * @param def
   * @return True if the configuration entry was updated/set
   */
  public boolean update(final String path, final Object def) {
    if (!isSet(path)) {
      set(path, def);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns a material based on it's enum name
   *
   * @param path
   * @return Returns the material or a BARRIER if it's invalid
   */
  public Material getMaterial(final String path) {
    return getMaterial(path, Material.BARRIER);
  }

  /**
   * Returns a material based on it's enum name
   *
   * @param path
   * @return Returns the material or the default material
   */
  public Material getMaterial(final String path, final Material def) {
    if (isSet(path)) {
      final boolean valid = EnumUtils.isValidEnum(Material.class, getString(path));
      if (!valid) {
        plugin.getLogger().severe("Invalid Material in config " + file.getName() + " at path: " + path + " No Material found for: " + getString(path));
        return def;
      }
      return Material.valueOf(getString(path));
    } else {
      return def;
    }
  }

  /**
   * Save the given Location to the configuration.
   *
   * @param path
   * @param location
   */
  public void setLocation(final String path, final Location location) {
    set(path, UtilLoc.locToString(location));
  }

  /**
   * Load a location from the configuration.
   *
   * @param path
   * @return The Location Object
   */
  @Override
  public Location getLocation(final String path) {
    return UtilLoc.locFromString(getString(path));
  }

  public void setInventoryContent(final String path, final Inventory inventory) {
    set(path, inventory.getContents());
  }

  public void setItemStackArray(final String path, final ItemStack[] array) {
    set(path, array);
  }

  public ItemStack[] getItemStackArray(final String path) {
    return ((List<ItemStack>) get(path)).toArray(new ItemStack[0]);
  }

  public ItemStack[] getInventoryContents(final String path, final ItemStack[] def) {
    return isSet(path) ? getItemStackArray(path) : def;
  }

  /**
   * Returns a Location from a config, or the default value if it's not saved.
   *
   * @param path
   * @param def
   * @return The Location object or the default value
   */
  @Override
  public Location getLocation(final String path, final Location def) {
    return isSet(path) ? UtilLoc.locFromString(getString(path)) : def;
  }

  public File getFile() {
    return file;
  }
}
