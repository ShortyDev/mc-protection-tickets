package at.shortydev.bot.commands.impl;

import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class GetTimestampCommand extends Command {

    public GetTimestampCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (args.length != 1) {
            EasyEmbed.builder()
                    .title("Wrong usage")
                    .description("Missing argument.")
                    .autoDelete(20)
                    .timestamp(Instant.now())
                    .color(Color.RED)
                    .build()
                    .buildMessageAndSend(channel);
            return;
        }
        try {
            long snowflake = Long.parseLong(args[0]);
            long timeMillis = (snowflake >> 22) + 1420070400000L;
            EasyEmbed.builder()
                    .title("Timestamp of discord id")
                    .description("Success for **" + args[0] + "**")
                    .fields(new ArrayList<>())
                    .timestamp(Instant.now())
                    .color(Color.RED)
                    .build()
                    .addField(new MessageEmbed.Field("Milliseconds", timeMillis + "ms", false))
                    .addField(new MessageEmbed.Field("GMT Timestamp", new Date(timeMillis).toGMTString(), false))
                    .buildMessageAndSend(channel);
        } catch (Exception exception) {
            EasyEmbed.builder()
                    .title("An unknown error occurred.")
                    .description("If you think this is fatal, contact the developer.")
                    .autoDelete(20)
                    .timestamp(Instant.now())
                    .color(Color.RED)
                    .build()
                    .buildMessageAndSend(channel);
        }
    }
}
