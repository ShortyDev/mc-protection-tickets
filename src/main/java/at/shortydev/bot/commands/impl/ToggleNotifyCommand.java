package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;

public class ToggleNotifyCommand extends Command {
    public ToggleNotifyCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.getPermissions().contains(Permission.ADMINISTRATOR) ||
                member.getRoles().contains(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Helper"))
                || member.getRoles().contains(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Jr.Helper"))) {
            Role ticketNotify = DiscordBot.getDiscordBot().getTicketListener().getTicketNotify();
            boolean notify = member.getRoles().contains(ticketNotify);
            if (notify) {
                channel.getGuild().removeRoleFromMember(member, ticketNotify).queue();
            } else {
                channel.getGuild().addRoleToMember(member, ticketNotify).queue();
            }
            EasyEmbed.builder()
                        .title("Toggled notify")
                        .description("You turned your ticket notify status **" + (notify ? "off" : "on") + "**.")
                        .timestamp(Instant.now())
                        .color(notify ? Color.RED : Color.GREEN)
                        .autoDelete(30)
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .build()
                        .buildMessageAndSend(channel);
        }
    }
}
