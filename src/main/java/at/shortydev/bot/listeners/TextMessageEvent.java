package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class TextMessageEvent extends ListenerAdapter {

    private final Random random = new Random();
    
    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        if (event.getAuthor().getId().equals(event.getGuild().getJDA().getSelfUser().getId())) return;
        if (event.getAuthor().getId().equals("691597756820619355"))
            Optional.ofNullable(event.getGuild().getEmotesByName(random.nextBoolean() ? "diepold" : "sadharold", false).get(0))
                    .filter(emote -> random.nextInt(20) > 18)
                    .ifPresent(emote -> event.getMessage().addReaction(emote).queue());
        DiscordBot.getDiscordBot().getMemberStatsController().logMessage(event.getAuthor().getId(), event.getChannel().getName(), event.getChannel().getId(), event.getMessage().getContentRaw());
        DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(event.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
            if (serverSettings == null)
                return;
            if (event.getMessage().getContentRaw().startsWith(serverSettings.getPrefix()))
                DiscordBot.getDiscordBot().getCommands().executeCommand(event.getTextChannel(), event.getMember(), event.getMessage().getContentRaw().substring(serverSettings.getPrefix().length()), event.getMessage());
        });
        if (DiscordBot.isTicketChannel(event.getTextChannel().getName())) {
            DiscordBot.getDiscordBot().getTicketDatabaseController().insert(event.getAuthor().getId(), event.getMessage().getContentRaw());
            DiscordBot.getDiscordBot().getTicketListener().getTicketLogs().sendMessage(event.getTextChannel().getName() + " - " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay().replace("everyone", "M:everyone").replace("here", "M:here").replace("*", "M:*")).queue();
            for (Message.Attachment attachment : event.getMessage().getAttachments()) {
                try {
                    DiscordBot.getDiscordBot().getTicketListener().getTicketLogs().sendFile(attachment.downloadToFile().get(), new AttachmentOption[]{}).queue();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        if (event.getMember() != null &&
                (event.getMessage().getContentRaw().toLowerCase().startsWith("-st") || event.getMessage().getContentRaw().toLowerCase().startsWith("-sudotag")) &&
                event.getMember().getRoles().stream().anyMatch(role -> role.getId().equals(DiscordBot.getDiscordBot().getTicketListener().getRoles().get("Helper").getId()))) {
            DiscordBot.getDiscordBot().getCommandStatsController().addCommandNow(event.getMember().getId());
        }
        try {
            DiscordBot.getDiscordBot().getMemberStatsController().updateData(event.getMember().getId(),
                    event.getMember().getUser().getEffectiveAvatarUrl(),
                    event.getAuthor().getName() +
                            "#" + event.getAuthor().getDiscriminator());
        } catch (Exception ignored) {}
    }
}
