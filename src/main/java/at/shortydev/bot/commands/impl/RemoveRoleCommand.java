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
import java.util.List;
import java.util.stream.Collectors;

public class RemoveRoleCommand extends Command {
    public RemoveRoleCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Helper").getId()))) {
            boolean admin = member.getPermissions().contains(Permission.ADMINISTRATOR);
            List<Member> mentioned = message.getMentionedMembers();
            if (mentioned.size() == 1) {
                if (args.length == 2) {
                    Member mentionedMember = mentioned.iterator().next();
                    if (args[0].contains(mentionedMember.getId())) {
                        Role role = DiscordBot.getDiscordBot().getTicketListener().getBuyerRoles().getOrDefault(args[1].toLowerCase(), null);
                        if (role != null) {
                            Role finalRole = role;
                            if (mentionedMember.getRoles().stream().anyMatch(memberRole -> memberRole.getId().equals(finalRole.getId()))) {
                                mentionedMember.getGuild().removeRoleFromMember(mentionedMember, role).queue();
                                EasyEmbed.builder()
                                        .title("Removed role")
                                        .description("Removed role from " + mentionedMember.getAsMention() + ".")
                                        .autoDelete(15)
                                        .color(Color.GREEN)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .build()
                                        .buildMessageAndSend(channel);
                            } else {
                                EasyEmbed.builder()
                                        .title("Role not found")
                                        .description(mentionedMember.getAsMention() + " hasn't got the role.")
                                        .autoDelete(15)
                                        .color(Color.RED)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .build()
                                        .buildMessageAndSend(channel);
                            }
                        } else {
                            if (!admin) {
                                EasyEmbed.builder()
                                        .title("Role not found")
                                        .description("Please use one of these role-types: " + DiscordBot.getDiscordBot().getTicketListener().getBuyerRoles().keySet().stream().map(s -> ", " + s).collect(Collectors.joining()).substring(2))
                                        .autoDelete(15)
                                        .color(Color.RED)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .build()
                                        .buildMessageAndSend(channel);
                                return;
                            }
                            role = mentionedMember.getGuild().getRoleById(args[1]);
                            if (role == null) {
                                EasyEmbed.builder()
                                        .title("Role not found")
                                        .description("There is no role with this id.")
                                        .autoDelete(15)
                                        .color(Color.RED)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .build()
                                        .buildMessageAndSend(channel);
                            } else {
                                Role finalRole1 = role;
                                if (mentionedMember.getRoles().stream().anyMatch(memberRole -> memberRole.getId().equals(finalRole1.getId()))) {
                                    mentionedMember.getGuild().removeRoleFromMember(mentionedMember, role).queue();
                                    EasyEmbed.builder()
                                            .title("Removed role")
                                            .description("Removed role from " + mentionedMember.getAsMention() + ".")
                                            .autoDelete(15)
                                            .color(Color.GREEN)
                                            .timestamp(Instant.now())
                                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                            .build()
                                            .buildMessageAndSend(channel);
                                } else {
                                    EasyEmbed.builder()
                                            .title("Role not found")
                                            .description(mentionedMember.getAsMention() + " hasn't got the role.")
                                            .autoDelete(15)
                                            .color(Color.RED)
                                            .timestamp(Instant.now())
                                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                            .build()
                                            .buildMessageAndSend(channel);
                                }
                            }
                        }
                    } else {
                        EasyEmbed.builder()
                                .title("Command arguments wrong")
                                .description("Please add exactly one member (ping them, **at the first argument**)")
                                .autoDelete(15)
                                .color(Color.RED)
                                .timestamp(Instant.now())
                                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                .build()
                                .buildMessageAndSend(channel);
                    }
                }
            } else {
                EasyEmbed.builder()
                        .title("Command arguments wrong")
                        .description("Please add exactly one member (ping them)")
                        .autoDelete(15)
                        .color(Color.RED)
                        .timestamp(Instant.now())
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .build()
                        .buildMessageAndSend(channel);
            }
        }
    }
}
