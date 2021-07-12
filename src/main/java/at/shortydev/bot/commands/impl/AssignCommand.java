package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class AssignCommand extends Command {

    public AssignCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.getRoles().stream().anyMatch(role -> role.getId().equals(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Helper").getId()))) {
            if (DiscordBot.isTicketChannel(channel.getName())) {
                String helperName = args.length > 0 ? Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1).toLowerCase(Locale.ROOT) : member.getUser().getName().toLowerCase(Locale.ROOT);
                if (createOrMove(helperName, channel)) {
                    EasyEmbed.builder()
                            .title("Channel moved")
                            .description("This ticket channel was moved to " + helperName + "'" + (helperName.endsWith("s") ? "" : "s") + " category.")
                            .color(Color.GREEN)
                            .autoDelete(5)
                            .timestamp(Instant.now())
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .build().buildMessageAndSend(channel);
                } else {
                    EasyEmbed.builder()
                            .title("Already moved")
                            .description("This ticket channel has already been moved to a category.")
                            .color(Color.RED)
                            .autoDelete(15)
                            .timestamp(Instant.now())
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .build().buildMessageAndSend(channel);
                }
            } else {
                EasyEmbed.builder()
                        .title("No ticket channel")
                        .description("This command can only be used in a ticket channel.")
                        .color(Color.RED)
                        .autoDelete(15)
                        .timestamp(Instant.now())
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .build().buildMessageAndSend(channel);
            }
        }
    }

    public boolean createOrMove(String helper, TextChannel textChannel) {
        Category category = textChannel.getGuild().getCategories().stream().filter(cat -> cat.getName().equals(helper)).findFirst().orElse(null);
        if (category == null) {
            textChannel.getManager().setParent(textChannel.getGuild().createCategory(helper).complete()).queue();
        } else {
            textChannel.getManager().setParent(category).queue();
        }
        return true;
    }
}
