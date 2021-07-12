package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import at.shortydev.bot.pastebin.PasteHelper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class SearchLogsCommand extends Command {
    public SearchLogsCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length > 0) {
                String query = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                Message toEdit = EasyEmbed.builder()
                        .title("Fetching...")
                        .description("Fetching results..")
                        .color(Color.ORANGE)
                        .timestamp(Instant.now())
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .build().buildMessageAndSend(channel);

                DiscordBot.getDiscordBot().getTicketDatabaseController().findMessages(query).thenAccept(strings -> {
                    if (strings.length > 20) {
                        String pasteLink = PasteHelper.createPaste(strings);
                        toEdit.editMessage(EasyEmbed.builder()
                                .title("Found " + strings.length + " results")
                                .description("Result size was too large. Moving to pastebin.\n" + pasteLink)
                                .color(Color.GREEN)
                                .timestamp(Instant.now())
                                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                .build().buildMessage()).queue();
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
                        toEdit.editMessage(EasyEmbed.builder()
                                .title("Found " + strings.length + " results")
                                .description(Arrays.stream(strings).map(s -> "\n-> " + s.split("%__\\$__%")[0] + " - " + simpleDateFormat.format(new Date(Long.parseLong(s.split("%__\\$__%")[1]))) + ": " + s.split("%__\\$__%")[2]).collect(Collectors.joining()))
                                .color(Color.GREEN)
                                .timestamp(Instant.now())
                                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                .build().buildMessage()).queue();
                    }
                });
            } else {
                EasyEmbed.builder()
                        .title("Search string missing")
                        .description("Please enter a search string.")
                        .color(Color.RED)
                        .autoDelete(15)
                        .timestamp(Instant.now())
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .build().buildMessageAndSend(channel);
            }
        }
    }
}
