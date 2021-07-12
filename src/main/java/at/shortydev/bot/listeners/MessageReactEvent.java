package at.shortydev.bot.listeners;

import at.shortydev.bot.reactions.EasyReaction;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactEvent extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        for (EasyReaction easyReaction : EasyReaction.REACTIONS) {
            if (easyReaction.getMessage() != 0 && easyReaction.getMessage() == event.getMessageIdLong()) {
                if (easyReaction.getEmote().equals(event.getReactionEmote().getEmote())) {
                    easyReaction.getOnReact().accept(EasyReaction.Reaction.builder().user(event.getMember()).action(EasyReaction.Reaction.Action.ADD).build());
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        for (EasyReaction easyReaction : EasyReaction.REACTIONS) {
            if (easyReaction.getMessage() != 0 && easyReaction.getMessage() == event.getMessageIdLong()) {
                if (easyReaction.getEmote().equals(event.getReactionEmote().getEmote())) {
                    easyReaction.getOnReact().accept(EasyReaction.Reaction.builder().user(event.getMember()).action(EasyReaction.Reaction.Action.REMOVE).build());
                }
            }
        }
    }
}
