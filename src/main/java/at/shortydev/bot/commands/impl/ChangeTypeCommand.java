package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.listeners.TicketListener;
import at.shortydev.bot.messages.EasyEmbed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ChangeTypeCommand extends Command {
    public ChangeTypeCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    public static TicketListener.TicketType getTicketType(String type) {
        return Arrays.stream(TicketListener.TicketType.values()).filter(ticketType -> ticketType.getName().equalsIgnoreCase(type)).findFirst().orElse(null);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (DiscordBot.isTicketChannel(channel.getName())) {
            DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(member.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
                if (args.length == 1) {
                    Message ticketInfoMessage = channel.getHistoryFromBeginning(1).complete().getRetrievedHistory().get(0);
                    if (ticketInfoMessage != null) {
                        if (ticketInfoMessage.isEdited()) {
                            EasyEmbed.builder()
                                    .title("Cannot change type")
                                    .description("The type has already been changed once.")
                                    .autoDelete(10)
                                    .color(Color.ORANGE)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .build()
                                    .buildMessageAndSend(channel);
                            return;
                        }
                        TicketInfo ticketInfo = TicketInfo.getInfo(ticketInfoMessage);
                        if (ticketInfo != null) {
                            TicketListener.TicketType type = getTicketType(args[0]);
                            if (type != null) {
                                if (ticketInfo.getType() != type) {
                                    ticketInfo = new TicketInfo(type, ticketInfo.getTicketCase(), ticketInfo.getCreatedBy());
                                    String name = type.getName().toLowerCase().substring(0, 2) + "-ticket-" + ticketInfo.getTicketCase();
                                    channel.getManager().setName(name).queue();
                                    EasyEmbed easyEmbed = EasyEmbed.builder()
                                            .title(ticketInfo.getType().getName() + " - Case #" + name.split("-")[2])
                                            .description("**Please ask your question directly.**\n \nTickets are logged so we can keep track of issues."
                                                    + (ticketInfo.getType().isUnsupported() ? "\n \n**" + ticketInfo.getType().getName() + " is outdated and we can't promise any solution to your regard.**" : "") + "\nThe ticket can be closed with **" + serverSettings.getPrefix() + "close**.")
                                            .color(Color.GREEN)
                                            .fields(new ArrayList<>())
                                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                            .timestamp(Instant.now()).build();
                                    easyEmbed.addField(new MessageEmbed.Field("Type", ticketInfo.getType().getName(), true));
                                    easyEmbed.addField(new MessageEmbed.Field("Case", name.split("-")[2], true));
                                    easyEmbed.addField(new MessageEmbed.Field("Created by", "<@" + ticketInfo.getCreatedBy() + ">", true));
                                    ticketInfoMessage.editMessage(easyEmbed.buildMessage()).queue();
                                    if (ticketInfo.getType() == TicketListener.TicketType.LICENSING) {
                                        channel.sendMessage("Hey. How can we help you?\n*Answer in this chat*\n \n» I want a license because I bought a product.\n» I need my license because I lost it.\n» I need a new license because my partner left.\n» Other reasons\n \nIf you need a new license, please directly show us your payment proof. (Paypal screenshot **and** Minemen/Polymart/Autobuy screenshot of product and account[email])").queue();
                                    }
                                    DiscordBot.getDiscordBot().getTicketListener().getTicketLogs().sendMessage(name + " **has been updated (new type).**").queue();
                                    channel.getRolePermissionOverrides().forEach(roleOverride -> {
                                        if (!roleOverride.getRole().getId().equals(channel.getGuild().getPublicRole().getId())) {
                                            roleOverride.delete().queue();
                                        }
                                    });
                                    channel.putPermissionOverride(channel.getGuild().getPublicRole()).setDeny(Permission.MESSAGE_READ).queue();
                                    for (Role role : type.getAllowedRoles()) {
                                        channel.putPermissionOverride(role)
                                                .setAllow(Permission.MESSAGE_READ)
                                                .queueAfter(2, TimeUnit.SECONDS);
                                    }
                                } else {
                                    EasyEmbed.builder()
                                            .title("Cannot change type")
                                            .description("This ticket is already a **" + type.getName() + "** ticket.")
                                            .autoDelete(10)
                                            .color(Color.ORANGE)
                                            .timestamp(Instant.now())
                                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                            .build()
                                            .buildMessageAndSend(channel);
                                }
                            } else {
                                StringBuilder types = new StringBuilder();
                                for (TicketListener.TicketType ticketType : TicketListener.TicketType.values()) {
                                    types.append(", ").append(ticketType.getName());
                                }
                                EasyEmbed.builder()
                                        .title("No ticket type found")
                                        .description("The provided ticket type is invalid.\nTypes: " + types.substring(2))
                                        .autoDelete(10)
                                        .color(Color.RED)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .build()
                                        .buildMessageAndSend(channel);
                            }
                        } else {
                            EasyEmbed.builder()
                                    .title("No ticket info found")
                                    .description("No ticket info has been found.")
                                    .autoDelete(10)
                                    .color(Color.RED)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .build()
                                    .buildMessageAndSend(channel);
                        }
                    } else {
                        EasyEmbed.builder()
                                .title("No ticket info found")
                                .description("No ticket info has been found.")
                                .autoDelete(10)
                                .color(Color.RED)
                                .timestamp(Instant.now())
                                .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                .build()
                                .buildMessageAndSend(channel);
                    }
                } else {
                    StringBuilder types = new StringBuilder();
                    for (TicketListener.TicketType ticketType : TicketListener.TicketType.values()) {
                        types.append(", ").append(ticketType.getName());
                    }
                    EasyEmbed.builder()
                            .title("Too little arguments")
                            .description("To change the ticket type, a type is required.\nTypes: " + types.substring(2))
                            .autoDelete(15)
                            .color(Color.RED)
                            .timestamp(Instant.now())
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .build()
                            .buildMessageAndSend(channel);
                }
            });
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class TicketInfo {

        private TicketListener.TicketType type;
        private int ticketCase;
        private String createdBy;

        public static TicketInfo getInfo(Message message) {
            TicketListener.TicketType ticketType = null;
            int ticketCase = 0;
            String createdBy = null;
            for (MessageEmbed.Field field : message.getEmbeds().get(0).getFields()) {
                if (field == null || field.getName() == null || field.getValue() == null) {
                    continue;
                }
                switch (field.getName()) {
                    case "Type":
                        ticketType = ChangeTypeCommand.getTicketType(field.getValue());
                        break;
                    case "Case":
                        ticketCase = Integer.parseInt(field.getValue());
                        break;
                    case "Created by":
                        createdBy = field.getValue().replace("<", "").replace(">", "").replace("@", "").replace("!", "");
                        break;
                }
            }
            if (ticketType != null && ticketCase != 0 && createdBy != null) {
                return builder().type(ticketType).ticketCase(ticketCase).createdBy(createdBy).build();
            }
            return null;
        }
    }
}
