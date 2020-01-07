package net.crytec.libs.commons.utils;

import net.crytec.libs.commons.utils.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class CommonsAPI {

  private static JavaPlugin host;
  private static String hostPrefix;

  public CommonsAPI(final JavaPlugin host) {
    CommonsAPI.host = host;
    hostPrefix = "[" + host.getName() + "] " + ChatColor.RESET;
  }

  public static JavaPlugin getHost() {
    return host;
  }

  public static String getHostPrefix() {
    return hostPrefix;
  }

  public void setHostPrefix(final String prefix) {
    hostPrefix = prefix + ChatColor.RESET;
    hostPrefix = StringUtils.strip(hostPrefix) + ChatColor.RESET;
  }
}