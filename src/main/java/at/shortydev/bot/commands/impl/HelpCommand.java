package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    public HelpCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (args.length == 0) {
            DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(channel.getGuild().getIdLong(), false).thenAccept(serverSettings -> EasyEmbed.builder()
                    .title("Help - Ticket Bot")
                    .color(Color.GREEN)
                    .description(DiscordBot.getDiscordBot().getCommands().getCommands().stream()
                            .filter(cmd -> !cmd.getName().equals("lookup"))
                            .map(cmd -> serverSettings.getPrefix() + cmd.getName() + " - " + cmd.getDescription() + "\n")
                            .collect(Collectors.joining()))
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .timestamp(Instant.now())
                    .autoDelete(20)
                    .build()
                    .buildMessageAndSend(channel));
        }
    }
}
