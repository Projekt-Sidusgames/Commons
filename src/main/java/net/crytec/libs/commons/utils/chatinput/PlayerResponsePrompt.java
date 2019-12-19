package net.crytec.libs.commons.utils.chatinput;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class PlayerResponsePrompt extends StringPrompt {

  private final String text;
  private final ChatInput input;

  public PlayerResponsePrompt(final ChatInput input, final String text) {
    this.text = text;
    this.input = input;
  }

  @Override
  public String getPromptText(final ConversationContext conversationContext) {
    return text;
  }

  @Override
  public Prompt acceptInput(final ConversationContext conversationContext, final String s) {
    input.accept(s);
    return Prompt.END_OF_CONVERSATION;
  }
}
