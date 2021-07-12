package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import at.shortydev.bot.panel.permissions.PanelPermissionGroup;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PanelCommand extends Command {

    public PanelCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            /*
            panel createuser <id>
            panel deluser <id>
            panel setgroup <id> <group>
            panel setpermissions <group> <permissions...>
            panel list
            panel permissions
            panel creategroup <name>
            panel delgroup <name>
            panel viewgroup <name> 
             */
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    DiscordBot.getDiscordBot().getPanelDatabaseController().getAllPermissionGroups().thenAccept(panelPermissionGroups -> EasyEmbed.builder()
                            .title("Panel list")
                            .description("**Permission groups**" + Arrays.stream(panelPermissionGroups).map(panelPermissionGroup ->
                                    "\n" + panelPermissionGroup.getName() + " | Permissions: " + panelPermissionGroup.getPermissions() + " | GRANT: " + panelPermissionGroup.isManagePermissions()).collect(Collectors.joining()))
                            .timestamp(Instant.now())
                            .color(Color.GREEN)
                            .autoDelete(60)
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .build()
                            .buildMessageAndSend(channel));
                } else if (args[0].equalsIgnoreCase("permissions")) {
                    EasyEmbed.builder()
                            .title("Permissions available")
                            .description(Arrays.stream(Permissions.values()).map(permission -> "\n" + permission.name() + " - " + permission.getInteger()).collect(Collectors.joining()).substring(1))
                            .color(Color.GREEN)
                            .autoDelete(180)
                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                            .timestamp(Instant.now())
                            .build()
                            .buildMessageAndSend(channel);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("createuser")) {
                    DiscordBot.getDiscordBot().getPanelDatabaseController().isUserExists(args[1]).thenAccept(exists -> {
                        if (!exists) {
                            DiscordBot.getDiscordBot().getPanelDatabaseController().createUser(args[1]);
                            EasyEmbed.builder()
                                    .title("Panel user created")
                                    .color(Color.GREEN)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .autoDelete(10)
                                    .build()
                                    .buildMessageAndSend(channel);
                        } else {
                            EasyEmbed.builder()
                                    .title("Panel user already exists")
                                    .color(Color.RED)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .autoDelete(10)
                                    .build()
                                    .buildMessageAndSend(channel);
                        }
                    });
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("setgroup")) {
                    String discordId = args[1];
                    String groupName = args[2];

                    DiscordBot.getDiscordBot().getPanelDatabaseController().getUserGroup(discordId).thenAccept(s -> DiscordBot.getDiscordBot().getPanelDatabaseController().hasGrantPermission(s).thenAccept(grant -> {
                        if (grant) {
                            DiscordBot.getDiscordBot().getPanelDatabaseController().isUserExists(discordId).thenAccept(exists -> DiscordBot.getDiscordBot().getPanelDatabaseController().getAllPermissionGroups().thenAccept(panelPermissionGroups -> {
                                PanelPermissionGroup permissionGroup = Arrays.stream(panelPermissionGroups).filter(panelPermissionGroup -> panelPermissionGroup.getName().equalsIgnoreCase(groupName)).findFirst().orElse(null);
                                if (permissionGroup != null) {
                                    DiscordBot.getDiscordBot().getPanelDatabaseController().updateUser(discordId, permissionGroup.getUuid());
                                    EasyEmbed.builder()
                                            .title("Panel user updated")
                                            .description("New permission group: " + permissionGroup.getName())
                                            .color(Color.RED)
                                            .timestamp(Instant.now())
                                            .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                            .autoDelete(10)
                                            .build()
                                            .buildMessageAndSend(channel);
                                    return;
                                }
                                EasyEmbed.builder()
                                        .title("Permission group doesn't exist")
                                        .color(Color.RED)
                                        .timestamp(Instant.now())
                                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                        .autoDelete(10)
                                        .build()
                                        .buildMessageAndSend(channel);
                            }));
                        } else {
                            EasyEmbed.builder()
                                    .title("Too low permissions")
                                    .description("You are missing the GRANT permission to use this command.")
                                    .color(Color.RED)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .autoDelete(10)
                                    .build()
                                    .buildMessageAndSend(channel);
                        }
                    }));
                } else if (args[0].equalsIgnoreCase("setpermissions")) {
                    DiscordBot.getDiscordBot().getPanelDatabaseController().getAllPermissionGroups().thenAccept(panelPermissionGroups -> {
                        PanelPermissionGroup permissionGroup = Arrays.stream(panelPermissionGroups).filter(panelPermissionGroup -> panelPermissionGroup.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);
                        if (permissionGroup != null) {
                            String permissions = args[2];
                            ArrayList<Permissions> permissionsArrayList = new ArrayList<>();
                            for (String permission : permissions.split(",")) {
                                if (permission.length() < 3) {
                                    continue;
                                }
                                permissionsArrayList.add(Permissions.valueOf(permission));
                            }
                            DiscordBot.getDiscordBot().getPanelDatabaseController().updateGroup(permissionGroup.getUuid(), Permissions.calc(permissionsArrayList.toArray(new Permissions[0])));
                        } else {
                            EasyEmbed.builder()
                                    .title("Permission group doesn't exist")
                                    .color(Color.RED)
                                    .timestamp(Instant.now())
                                    .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                                    .autoDelete(10)
                                    .build()
                                    .buildMessageAndSend(channel);
                        }
                    });
                }
            }
        }
    }

    @Getter
    public enum Permissions {

        NONE(0),
        VIEW_EARNINGS(1),
        VIEW_CUSTOMER_AMOUNT(2),
        VIEW_LSR(4),
        VIEW_DL(8),
        VIEW_PRODUCTS(16),
        EDIT_PRODUCTS(32),
        DEL_PRODUCTS(64),
        PUB_PRODUCTS(128),
        VIEW_CHARTS(256),
        VIEW_USERS(512),
        CD_USERS(1024),
        L_VIEW_BASICS(2048),
        L_VIEW_KEY(4096),
        L_VIEW_IPS(8192),
        L_VIEW_IP_LIMIT(16384),
        L_R_IP_LIMIT(32768),
        L_EDIT_IP(65536),
        L_DELETE(131072),
        VIEW_PURCHASES(262144),
        VIEW_COUPONS(524288),
        CREATE_COUPONS(1048576),
        DELETE_COUPONS(2097152),
        MODIFY_COUPONS(4194304),
        VIEW_DISCORD_STATS(8388608),
        VIEW_PURCHASES_DETAILED(16777216),
        ALL(33554432);

        private final int integer;

        Permissions(int integer) {
            this.integer = integer;
        }

        public static int calc(Permissions... permissions) {
            return Arrays.stream(permissions).mapToInt(Permissions::getInteger).sum();
        }
    }

}
