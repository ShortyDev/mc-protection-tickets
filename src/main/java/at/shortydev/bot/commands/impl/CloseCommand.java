package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class CloseCommand extends Command {
    public CloseCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (DiscordBot.isTicketChannel(channel.getName())) {
            channel.getManager().setName(channel.getName() + "-c").queue();
            EasyEmbed.builder()
                    .title("Ticket closed")
                    .description("This ticket was closed by " + member.getAsMention() + ".\nThe channel will be deleted in 10 seconds.")
                    .color(Color.RED)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .timestamp(Instant.now())
                    .build()
                    .buildMessageAndSend(channel);
            DiscordBot.getDiscordBot().getTicketListener().getTicketLogs().sendMessage(channel.getName() + " **has been closed.**").queueAfter(10, TimeUnit.SECONDS);
            channel.delete().queueAfter(10, TimeUnit.SECONDS);
        }
    }
}
