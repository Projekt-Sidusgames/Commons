package net.crytec.libs.commons.utils.chat;

import java.util.UUID;
import java.util.function.BiConsumer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ChatMessage {

  private final ComponentBuilder builder;

  public ChatMessage(final String message) {
    builder = new ComponentBuilder(new TextComponent(message));
  }

  public void addString(final String string) {
    builder.append(string);
  }

  public void appendClickHandler(final String string, final String hover, final BiConsumer<Player, String[]> clickHandler) {
    final TextComponent clickText = new TextComponent(string);
    final UUID id = UUID.randomUUID();
    clickText.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/ccline " + id.toString()));

    if (hover != null) {
      clickText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").appendLegacy(hover).create()));
    }

    builder.append(clickText, FormatRetention.NONE);
    ChatHandler.get().register(id, clickHandler);
  }

  public BaseComponent[] get() {
    return builder.create();
  }

  public void send(final Player player) {
    player.spigot().sendMessage(builder.create());
  }

}