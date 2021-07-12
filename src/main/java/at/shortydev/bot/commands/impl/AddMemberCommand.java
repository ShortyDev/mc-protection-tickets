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
import java.util.regex.Pattern;

public class AddMemberCommand extends Command {
    public AddMemberCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.getRoles().contains(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Helper"))) {
            if (!member.getPermissions().contains(Permission.ADMINISTRATOR) && !DiscordBot.isTicketChannel(channel.getName())) {
                EasyEmbed.builder()
                        .title("Not enough privileges")
                        .description("You can't add a user to this channel. You can only add users to channels like: " + (Pattern.compile("^[a-z]{2}-ticket-\\d{4,5}").pattern()))
                        .color(Color.RED)
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .timestamp(Instant.now())
                        .autoDelete(10)
                        .build()
                        .buildMessageAndSend(channel);
                return;
            }
            Member mentioned = message.getMentionedMembers().stream().findFirst().orElse(null);
            if (mentioned == null) {
                EasyEmbed.builder()
                        .title("User is null")
                        .description("The mentioned member doesn't exist.\nNo member was mentioned.")
                        .color(Color.RED)
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .timestamp(Instant.now())
                        .autoDelete(10)
                        .build()
                        .buildMessageAndSend(channel);
                return;
            }
            EasyEmbed.builder()
                    .title("User added")
                    .description("The mentioned member has been added to this channel.")
                    .color(Color.GREEN)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .timestamp(Instant.now())
                    .autoDelete(10)
                    .build()
                    .buildMessageAndSend(channel);
            channel.putPermissionOverride(mentioned)
                    .setAllow(Permission.MESSAGE_READ)
                    .queue();
        }
    }
}
