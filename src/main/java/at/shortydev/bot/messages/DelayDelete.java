package at.shortydev.bot.messages;

import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class DelayDelete {

    public DelayDelete(Message message, int delay) {
        message.delete().queueAfter(delay * 1000L, TimeUnit.MILLISECONDS);
    }
}
