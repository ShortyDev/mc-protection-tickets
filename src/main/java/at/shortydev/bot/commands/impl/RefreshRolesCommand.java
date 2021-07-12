package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class RefreshRolesCommand extends Command {

    private static final Map<String, Long> commandCoolDown = new HashMap<>();

    public RefreshRolesCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (commandCoolDown.getOrDefault(member.getId(), 0L) - System.currentTimeMillis() < 0) {
            commandCoolDown.put(member.getId(), System.currentTimeMillis() + 60000);
            EasyEmbed.builder()
                    .title("Updated roles")
                    .description("Your roles in the database have been updated.")
                    .color(Color.GREEN)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .timestamp(Instant.now())
                    .autoDelete(20)
                    .build()
                    .buildMessageAndSend(channel);
            DiscordBot.getDiscordBot().getRoleDatabaseController().saveRoles(member);
        } else {
            EasyEmbed.builder()
                    .title("Cooldown... please wait")
                    .description("This command is on cooldown, please wait.")
                    .color(Color.RED)
                    .timestamp(Instant.now())
                    .autoDelete(20)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .build()
                    .buildMessageAndSend(channel);
        }
    }
}
