package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class SetPrefixCommand extends Command {
    public SetPrefixCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length == 1) {
                if (DiscordBot.isValidPrefix(args[0])) {
                    if (args[0].length() > 0) {
                        if (args[0].length() < 5) {
                            DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(channel.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
                                if (serverSettings == null)
                                    return;
                                serverSettings.setPrefix(args[0]);
                                DiscordBot.getDiscordBot().getServerDatabaseController().getSettingsCache().put(channel.getGuild().getIdLong(), serverSettings);
                                EasyEmbed.builder()
                                        .title("Prefix set")
                                        .description("The new command prefix has successfully been set to **" + args[0] + "**.\nExample: " + args[0] + "help")
                                        .color(Color.GREEN)
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .timestamp(Instant.now())
                                        .build()
                                        .buildMessageAndSend(channel);
                            });
                        } else {
                            EasyEmbed.builder()
                                    .title("Too much characters")
                                    .description("The first argument has **too much characters**, allowed is **max. 4**.")
                                    .autoDelete(10)
                                    .color(Color.RED)
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .timestamp(Instant.now())
                                    .build()
                                    .buildMessageAndSend(channel);
                        }
                    } else {
                        EasyEmbed.builder()
                                .title("Too less characters")
                                .description("The first argument has to have at **least 1 character**.")
                                .autoDelete(10)
                                .color(Color.RED)
                                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                .timestamp(Instant.now())
                                .build()
                                .buildMessageAndSend(channel);
                    }
                } else {
                    EasyEmbed.builder()
                            .title("Argument not alphanumeric")
                            .description("The first argument isn't alphanumeric. Please only use **[a-zA-Z0-9]**.")
                            .autoDelete(10)
                            .color(Color.RED)
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .timestamp(Instant.now())
                            .build()
                            .buildMessageAndSend(channel);
                }
            } else {
                if (args.length > 1) {
                    EasyEmbed.builder()
                            .title("Max. argument length exceeded")
                            .description("The maximum arguments for a prefix are 1.")
                            .autoDelete(10)
                            .color(Color.RED)
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .timestamp(Instant.now())
                            .build()
                            .buildMessageAndSend(channel);
                } else {
                    EasyEmbed.builder()
                            .title("Not enough arguments")
                            .description("The minimum arguments for a prefix are 1.")
                            .autoDelete(10)
                            .color(Color.RED)
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .timestamp(Instant.now())
                            .build()
                            .buildMessageAndSend(channel);
                }
            }
        }
    }
}
