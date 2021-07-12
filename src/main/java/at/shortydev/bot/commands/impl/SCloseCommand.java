package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class SCloseCommand extends Command {
    public SCloseCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        EasyEmbed.builder()
                .title("No further questions? Close the ticket.")
                .description("To close the ticket, you can use **.close**. Thanks for using our ticket system.")
                .color(Color.getHSBColor(175, 100, 29))
                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                .timestamp(Instant.now())
                .build()
                .buildMessageAndSend(channel);
    }
}
