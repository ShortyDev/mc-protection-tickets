package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.messages.EasyEmbed;
import at.shortydev.bot.reactions.EasyReaction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Getter
public class TicketListener {

    private final Map<String, Role> roles = new HashMap<>();
    private final Map<String, Role> buyerRoles = new HashMap<>();
    private final Map<String, Long> delay = new HashMap<>();
    private Guild server;
    private Role ticketNotify;
    private VoiceChannel ticketCount;
    private Category ticketCategory;
    private Category vipTicketCategory;
    private TextChannel ticketChannel;
    private TextChannel ticketLogs;

    public void start() {
        for (Guild guild : DiscordBot.getDiscordBot().getShardManager().getGuilds()) {
            if (guild.getId().equals("665338724703273000")) {
                long ticketMessage = 766677961243492382L;
                server = guild;
                ticketChannel = guild.getTextChannelById(766148731397341185L);
                ticketCount = guild.getVoiceChannelById(766681923253698610L);
                ticketCategory = guild.getCategoryById(766171431125385216L);
                vipTicketCategory = guild.getCategoryById(766682045165731850L);
                ticketNotify = guild.getRoleById(766682128275996673L);
                ticketLogs = guild.getTextChannelById(766682341879316480L);

                buyerRoles.put("aegis", guild.getRoleById(766146859075895298L));
                buyerRoles.put("spigotguard", guild.getRoleById(766146893570637844L));
                buyerRoles.put("atomspigot", guild.getRoleById(766147008397574164L));
                buyerRoles.put("lobbysystem", guild.getRoleById(766147346336972860L));
                buyerRoles.put("casualprotector", guild.getRoleById(766147394285862933L));
                buyerRoles.put("cerberus", guild.getRoleById(766146930165415967L));
                buyerRoles.put("partner", guild.getRoleById(766245127534411797L));
                buyerRoles.put("youtuber", guild.getRoleById(766188251656224800L));

                roles.put("Helper", guild.getRoleById(766144596426096641L));
                roles.put("Jr.Helper", guild.getRoleById(766145951349211136L));
                roles.put("Cerberus Dev", guild.getRoleById(766144868929896458L));
                roles.put("AtomSpigot Dev", guild.getRoleById(766653961879945236L));

                Emote aegis = guild.getEmotesByName("aegis_v1", true).stream().findFirst().orElse(null);
                Emote spigotguard = guild.getEmotesByName("spigotguard_v1", true).stream().findFirst().orElse(null);
                Emote atomspigot = guild.getEmotesByName("atomspigot_v1", true).stream().findFirst().orElse(null);
                Emote lobbysystem = guild.getEmotesByName("lobbysystem_v1", true).stream().findFirst().orElse(null);
                Emote casualprotector = guild.getEmotesByName("cprotector_v1", true).stream().findFirst().orElse(null);
                Emote cerberus = guild.getEmotesByName("cerberusac_v1", true).stream().findFirst().orElse(null);
                Emote general = guild.getEmotesByName("envelope", true).stream().findFirst().orElse(null);
                Emote licensing = guild.getEmotesByName("vote_yes", true).stream().findFirst().orElse(null);

                TicketType.AEGIS.setAllowedRoles(roles.get("Helper"));
                TicketType.SPIGOTGUARD.setAllowedRoles(roles.get("Helper"));
                TicketType.ATOMSPIGOT.setAllowedRoles(roles.get("Helper"), roles.get("AtomSpigot Dev"));
                TicketType.LOBBYSYSTEM.setAllowedRoles(roles.get("Helper"));
                TicketType.CASUALPROTECTOR.setAllowedRoles(roles.get("Helper"), roles.get("Jr.Helper"));
                TicketType.CERBERUSANTICHEAT.setAllowedRoles(roles.get("Helper"), roles.get("Cerberus Dev"));
                TicketType.GENERAL.setAllowedRoles(roles.get("Helper"));
                TicketType.LICENSING.setAllowedRoles(roles.get("Helper"));

                if (ticketChannel == null)
                    return;

                System.out.println("Started TicketListener");
                EasyReaction.builder()
                        .emote(aegis)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, aegis, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.AEGIS);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(spigotguard)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, spigotguard, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.SPIGOTGUARD);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(atomspigot)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, atomspigot, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.ATOMSPIGOT);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(lobbysystem)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, lobbysystem, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.LOBBYSYSTEM);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(cerberus)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, cerberus, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.CERBERUSANTICHEAT);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(casualprotector)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, casualprotector, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.CASUALPROTECTOR);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(licensing)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, licensing, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.LICENSING);
                        }).build()
                        .registerReaction();
                EasyReaction.builder()
                        .emote(general)
                        .message(ticketMessage)
                        .onReact(reaction -> {
                            if (reaction.getAction() != EasyReaction.Reaction.Action.ADD)
                                return;
                            ticketChannel.removeReactionById(ticketMessage, general, reaction.getUser().getUser()).queue();
                            if (delay.containsKey(reaction.getUser().getId()) && delay.getOrDefault(reaction.getUser().getId(), System.currentTimeMillis() + 60000) - System.currentTimeMillis() > 0) {
                                return;
                            }
                            delay.put(reaction.getUser().getId(), System.currentTimeMillis() + 60000);
                            openTicket(reaction.getUser(), TicketType.GENERAL);
                        }).build()
                        .registerReaction();
            }
        }
    }

    public void openTicket(Member user, TicketType ticketType) {
        DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(user.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
            String channelName = ticketType.getName().toLowerCase().substring(0, 2) + "-ticket-" + ticketCount.getName().split(" ")[1];
            TextChannel textChannel = user.getGuild().createTextChannel(channelName).complete();
            textChannel.getManager().setParent(user.getRoles().stream().anyMatch(role -> role.getId().equals("766683061420294174")) ? vipTicketCategory : ticketCategory).complete();
            textChannel.createPermissionOverride(server.getPublicRole())
                    .setDeny(Permission.MESSAGE_READ)
                    .queue();
            textChannel.createPermissionOverride(user)
                    .setAllow(Permission.MESSAGE_READ)
                    .queue();
            for (Role role : ticketType.getAllowedRoles()) {
                textChannel.createPermissionOverride(role)
                        .setAllow(Permission.MESSAGE_READ)
                        .queue();
            }
            EasyEmbed.builder()
                    .title("New ticket opened")
                    .description("A new ticket has been opened " + user.getUser().getAsMention() + "!\nIt appears at the bottom of the channel list (" + textChannel.getAsMention() + ")")
                    .color(Color.GREEN)
                    .timestamp(Instant.now())
                    .autoDelete(10)
                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                    .build()
                    .buildMessageAndSend(ticketChannel);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MILLISECONDS.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    EasyEmbed easyEmbed = EasyEmbed.builder()
                            .title(ticketType.getName() + " - Case #" + ticketCount.getName().split(" ")[1])
                            .description("**Please ask your question directly.**\n \nTickets are logged so we can keep track of issues."
                                    + (ticketType.isUnsupported() ? "\n \n**" + ticketType.getName() + " is outdated and we can't promise any solution to your regard.**" : "") + "\nThe ticket can be closed with **" + serverSettings.getPrefix() + "close**.")
                            .color(Color.GREEN)
                            .fields(new ArrayList<>())
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .timestamp(Instant.now()).build();
                    easyEmbed.addField(new MessageEmbed.Field("Type", ticketType.getName(), true));
                    easyEmbed.addField(new MessageEmbed.Field("Case", channelName.split("-")[2], true));
                    easyEmbed.addField(new MessageEmbed.Field("Created by", user.getAsMention(), true));
                    easyEmbed.buildMessageAndSend(textChannel);
                    Message message = textChannel.sendMessage(user.getAsMention() + " " + ticketNotify.getAsMention()).complete();
                    message.delete().queue();
                    if (ticketType == TicketType.LICENSING)
                        textChannel.sendMessage("Hey. How can we help you?\n*Answer in this chat*\n \n» I want a license because I bought a product.\n» I need my license because I lost it.\n» I need a new license because my partner left.\n» Other reasons\n \nIf you need a new license, please directly show us your payment proof. (Paypal screenshot **and** Minemen/Polymart/Autobuy screenshot of product and account[email])").queue();
                    else
                        textChannel.sendMessage(":warning: Hey! This is very **important**. **If you need anything related to licenses, use the \".changetype Licensing\" command!** :warning:").queue();

                    ticketLogs.sendMessage(channelName + " **has been opened.**").queue();

                    ticketCount.getManager().setName(ticketCount.getName().split(" ")[0] + " " + (Integer.parseInt(ticketCount.getName().split(" ")[1]) + 1)).queue();
                }
            };
            DiscordBot.executor.execute(timerTask);
        });
    }

    @Getter
    @RequiredArgsConstructor
    public enum TicketType {
        AEGIS("Aegis", false), SPIGOTGUARD("SpigotGuard", false), ATOMSPIGOT("AtomSpigot", false),
        CERBERUSANTICHEAT("Cerberus", false), CASUALPROTECTOR("CasualProtector", true),
        LOBBYSYSTEM("LobbySystem", false), GENERAL("General", false), LICENSING("Licensing", false);

        private final String name;
        private final boolean unsupported;
        private Role[] allowedRoles;

        public void setAllowedRoles(Role... allowedRoles) {
            this.allowedRoles = allowedRoles;
        }
    }
}
