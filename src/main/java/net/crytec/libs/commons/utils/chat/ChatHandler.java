package net.crytec.libs.commons.utils.chat;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.crytec.libs.commons.utils.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatHandler implements Listener {

  private static ChatHandler instance;

  private final Map<UUID, BiConsumer<Player, String[]>> clickCache = Maps.newHashMap();

  public ChatHandler(final JavaPlugin host) {
    instance = this;
    Bukkit.getPluginManager().registerEvents(this, host);
  }

  public static ChatHandler get() {
    return instance;
  }

  public void register(final UUID uuid, final BiConsumer<Player, String[]> consumer) {
    clickCache.put(uuid, consumer);
  }

  public void unregister(final UUID uuid) {
    clickCache.remove(uuid);
  }

  @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
  public void parse(final PlayerCommandPreprocessEvent event) {
    if (!event.getMessage().isEmpty() && !event.getMessage().startsWith("/ccline")) {
      return;
    }
    event.setCancelled(true);

    final UUID id;

    try {
      id = UUID.fromString(event.getMessage().split(" ")[1]);
    } catch (final IllegalArgumentException ex) {
      return;
    }

    if (!clickCache.containsKey(id)) {
      return;
    }

    final String[] args = ArrayUtils.remove(event.getMessage().split(" "), 1);
    clickCache.get(id).accept(event.getPlayer(), args);
    clickCache.remove(id);
  }
}