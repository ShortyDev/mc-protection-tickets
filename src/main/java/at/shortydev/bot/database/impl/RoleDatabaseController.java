package at.shortydev.bot.database.impl;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RoleDatabaseController {

    public void saveRoles(Member member) {
        userExists(member).thenAccept(exists -> {
            if (!exists) {
                try {
                    PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO dc_roles(ID, ROLES) VALUES (?,?)");
                    preparedStatement.setString(1, member.getId());
                    preparedStatement.setString(2, getRoleString(member));
                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                try {
                    PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE dc_roles SET ROLES=? WHERE ID=?");
                    preparedStatement.setString(1, getRoleString(member));
                    preparedStatement.setString(2, member.getId());
                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    public void deleteRoles(Member member) {
        try {
            PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("DELETE FROM dc_roles WHERE ID=?");
            preparedStatement.setString(1, member.getId());
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public CompletableFuture<Boolean> userExists(Member member) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        try {
            PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM dc_roles WHERE ID=?");
            preparedStatement.setString(1, member.getId());
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                if (resultSet != null) {
                    try {
                        if (resultSet.next()) {
                            completableFuture.complete(true);
                        } else {
                            completableFuture.complete(false);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return completableFuture;
    }
    
    public void updateRoles(Role[] roles) {
        deleteAllRoles();
        String statement = "INSERT INTO discord_roles (NAME, ID) VALUES " + Arrays.stream(roles).map(s -> ", ('" + s.getName() + "', '" + s.getId() + "')").collect(Collectors.joining()).substring(2);
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare(statement);
        DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
    }

    private void deleteAllRoles() {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("DELETE FROM discord_roles");
        DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
    }

    public String getRoleString(Member member) {
        return member.getRoles().stream().map(role -> ";" + role.getId()).collect(Collectors.joining()).substring(1);
    }
}
