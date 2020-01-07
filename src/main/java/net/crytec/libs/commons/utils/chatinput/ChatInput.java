package net.crytec.libs.commons.utils.chatinput;

import java.util.function.Consumer;
import net.crytec.libs.commons.utils.CommonsAPI;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class ChatInput {

  private final Consumer<String> input;

  public ChatInput(final Player player, final String text, final Consumer<String> input) {
    this(player, text, true, input);
  }

  public ChatInput(final Player player, final String text, final boolean withPluginPrefix, final Consumer<String> input) {
    this.input = input;
    player.closeInventory();

    final ConversationFactory factory = new ConversationFactory(CommonsAPI.getHost());
    factory.withFirstPrompt(new PlayerResponsePrompt(this, text));
    factory.withLocalEcho(false);
    if (withPluginPrefix) {
      factory.withPrefix(conversationContext -> CommonsAPI.getHostPrefix());
    }
    player.beginConversation(factory.buildConversation(player));
  }

  protected void accept(final String input) {
    this.input.accept(input);
  }
}