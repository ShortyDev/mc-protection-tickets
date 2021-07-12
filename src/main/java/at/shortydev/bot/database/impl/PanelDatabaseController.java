package at.shortydev.bot.database.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.panel.permissions.PanelPermissionGroup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PanelDatabaseController {
    
    public CompletableFuture<PanelPermissionGroup[]> getAllPermissionGroups() {
        CompletableFuture<PanelPermissionGroup[]> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM pp_groups");
        DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
            try {
                List<PanelPermissionGroup> panelPermissionGroups = new ArrayList<>();
                while (resultSet.next()) {
                    panelPermissionGroups.add(new PanelPermissionGroup(resultSet.getString("UUID"), resultSet.getString("NAME"), 
                            resultSet.getInt("PERMISSIONS"), resultSet.getBoolean("MANAGE_PERMISSIONS")));
                }
                completableFuture.complete(panelPermissionGroups.toArray(new PanelPermissionGroup[0]));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return completableFuture;
    }

    public CompletableFuture<Boolean> isUserExists(String discordID) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM pp_users WHERE ID=?");
        try {
            preparedStatement.setString(1, discordID);
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                try {
                    completableFuture.complete(resultSet.next());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completableFuture;
    }

    public CompletableFuture<Boolean> hasGrantPermission(String groupId) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM pp_groups WHERE UUID=? AND MANAGE_PERMISSIONS=?");
        try {
            preparedStatement.setString(1, groupId);
            preparedStatement.setInt(2, 1);
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                try {
                    completableFuture.complete(resultSet.next());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completableFuture;
    }

    public CompletableFuture<String> getUserGroup(String discordId) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM pp_users WHERE ID=?");
        try {
            preparedStatement.setString(1, discordId);
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                try {
                    if (resultSet.next()) {
                        completableFuture.complete(resultSet.getString("G_UID"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completableFuture;
    }

    public void createUser(String discordId) {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO pp_users (ID, G_UID, LAST_LOGIN) VALUES (?,'609b6216-b1f7-40fe-997b-5f1f975cc712', '" + System.currentTimeMillis() + "')");
        try {
            preparedStatement.setString(1, discordId);
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateUser(String discordId, String groupId) {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE pp_users SET G_UID=? WHERE ID=?");
        try {
            preparedStatement.setString(1, groupId);
            preparedStatement.setString(2, discordId);
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGroup(String groupId, int newPermissionInteger) {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE pp_groups SET PERMISSIONS=? WHERE UUID=?");
        try {
            preparedStatement.setInt(1, newPermissionInteger);
            preparedStatement.setString(2, groupId);
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
