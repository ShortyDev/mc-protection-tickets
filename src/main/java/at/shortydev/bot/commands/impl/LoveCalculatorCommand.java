package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoveCalculatorCommand extends Command {
    public LoveCalculatorCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        String commandString = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining());
        if (args.length > 1 && commandString.contains(",")) {
            String name1 = commandString.split(",")[0].trim();
            String name2 = commandString.split(",")[1].trim();
            double loveResult = StringUtils.getJaroWinklerDistance(name1, name2) * 100;
            EasyEmbed.builder()
                    .title("Love result")
                    .description("The love strength between **" + name1 + "** and **" + name2 + "** is **" + loveResult + "%**.")
                    .color(Color.GRAY)
                    .timestamp(Instant.now())
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .build()
                    .buildMessageAndSend(channel);
        } else {
            EasyEmbed.builder()
                    .title("Too little arguments or split missing")
                    .description("Please enter at least 2 names, separated by \",\"")
                    .color(Color.RED)
                    .autoDelete(20)
                    .timestamp(Instant.now())
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .build()
                    .buildMessageAndSend(channel);
        }
    }
}
