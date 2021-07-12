package at.shortydev.bot.reactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * EasyReaction is a simple reaction management class developed by Shorty
 *
 * @author Jakob
 * @version 1.0
 */
@Builder
@Getter
public class EasyReaction {

    public static final List<EasyReaction> REACTIONS = new ArrayList<>();

    private final long message;
    private final Emote emote;
    private final Consumer<Reaction> onReact;

    public void registerReaction() {
        REACTIONS.add(this);
    }

    public void unregisterReaction() {
        REACTIONS.remove(this);
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Reaction {
        private final Member user;
        private final Action action;

        public enum Action {
            ADD, REMOVE
        }
    }
}
