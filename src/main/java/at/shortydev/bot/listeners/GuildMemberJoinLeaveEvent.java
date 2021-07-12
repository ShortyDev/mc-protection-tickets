package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoinLeaveEvent extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        DiscordBot.getDiscordBot().getMemberStatsController().insertJoin(event.getMember().getId(), event.getGuild(), true);
        DiscordBot.getDiscordBot().getMemberStatsController().updateData(event.getMember().getId(), event.getUser().getAvatarUrl(), event.getUser().getName() + "#" + event.getUser().getDiscriminator());
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        DiscordBot.getDiscordBot().getMemberStatsController().insertJoin(event.getUser().getId(), event.getGuild(), false);        
    }
}
