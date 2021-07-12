package at.shortydev.bot.commands.impl;

import at.shortydev.bot.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TranslateCommand extends Command {
    public TranslateCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (args.length > 0) {
            String textToTranslate = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);
        }
    }
}
