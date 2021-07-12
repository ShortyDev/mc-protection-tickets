package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberRoleAddEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@NotNull net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent event) {
        DiscordBot.getDiscordBot().getRoleDatabaseController().saveRoles(event.getMember());
    }
}
