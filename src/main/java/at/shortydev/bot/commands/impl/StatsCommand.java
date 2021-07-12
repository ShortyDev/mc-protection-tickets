package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class StatsCommand extends Command {
    public StatsCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(channel.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
            EasyEmbed.builder()
                    .title("Stats | " + channel.getGuild().getName())
                    .description("**ServerId** " + serverSettings.getId()
                            + "\n**Date joined** " + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(new Date(serverSettings.getBotJoined()))
                            + "\n**Commands executed** " + serverSettings.getCommandsUsed()
                            + "\n**Prefix** " + serverSettings.getPrefix())
                    .color(Color.GRAY)
                    .timestamp(Instant.now())
                    .autoDelete(20)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .build()
                    .buildMessageAndSend(channel);
        });
    }
}
