package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.settings.ServerSettings;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoinEvent extends ListenerAdapter {

    @SneakyThrows
    @Override
    public void onGuildJoin(@NotNull net.dv8tion.jda.api.events.guild.GuildJoinEvent event) {
        if (!DiscordBot.getDiscordBot().getServerDatabaseController().getSettingsCache().containsKey(event.getGuild().getIdLong())) {
            DiscordBot.getDiscordBot().getServerDatabaseController().createServerSettings(ServerSettings.builder().prefix("-").id(event.getGuild().getIdLong()).commandsUsed(0).botJoined(System.currentTimeMillis()).build());
        }
    }
}
