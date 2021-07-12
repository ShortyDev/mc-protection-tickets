package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberRoleRemoveEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleRemove(@NotNull net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent event) {
        DiscordBot.getDiscordBot().getRoleDatabaseController().saveRoles(event.getMember());
    }
}
