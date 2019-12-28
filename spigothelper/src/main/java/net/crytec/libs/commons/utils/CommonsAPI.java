package net.crytec.libs.commons.utils;

import org.bukkit.plugin.java.JavaPlugin;

public class CommonsAPI {

  private static JavaPlugin host;

  public CommonsAPI(final JavaPlugin host) {
    CommonsAPI.host = host;
  }

  public static JavaPlugin getHost() {
    return host;
  }

}
