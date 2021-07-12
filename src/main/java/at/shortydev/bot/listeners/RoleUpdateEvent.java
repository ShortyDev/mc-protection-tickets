package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RoleUpdateEvent extends ListenerAdapter {
    
    @Override
    public void onRoleUpdateName(@NotNull RoleUpdateNameEvent event) {
        DiscordBot.getDiscordBot().getRoleDatabaseController().updateRoles(event.getGuild().getRoles().toArray(new Role[0]));
    }
    
    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event) {
        DiscordBot.getDiscordBot().getRoleDatabaseController().updateRoles(event.getGuild().getRoles().toArray(new Role[0]));
    }
    
    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        DiscordBot.getDiscordBot().getRoleDatabaseController().updateRoles(event.getGuild().getRoles().toArray(new Role[0]));
    }
}
