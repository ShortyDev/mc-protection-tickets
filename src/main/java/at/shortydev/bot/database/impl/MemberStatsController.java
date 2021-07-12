package at.shortydev.bot.database.impl;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MemberStatsController {

    private static final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");

    public void insertJoin(String id, Guild guild, boolean isJoin) {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO memberActions(ACTION, ID, DAY, MILLIS, NEW_AMOUNT) VALUES (?, ?, ?, ?, ?)");
        try {
            preparedStatement.setString(1, isJoin ? "join" : "leave");
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, date.format(new Date(System.currentTimeMillis())));
            preparedStatement.setLong(4, System.currentTimeMillis());
            preparedStatement.setInt(5, guild.getMemberCount());

            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logMessage(String id, String channel_name, String channelId, String message) {
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO messageLogs(ID, DAY, MILLIS, MESSAGE, CHANNEL_ID, CHANNEL_NAME) VALUES (?, ?, ?, ?, ?, ?)");
        try {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, date.format(new Date(System.currentTimeMillis())));
            preparedStatement.setLong(3, System.currentTimeMillis());
            preparedStatement.setString(4, message);
            preparedStatement.setString(5, channelId);
            preparedStatement.setString(6, channel_name);

            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
    function updateData($id, $avatar, $name)
{
    global $conn;

    if (getAvatar($id) == null) {
        $stmt = $conn->prepare("INSERT INTO pp_userdata(ID, AVATAR, NAME) VALUES (?,?,?)");
        $stmt->bind_param("sss", $id, $avatar, $name);
        $stmt->execute();
        $stmt->close();
    } else {
        $stmt = $conn->prepare("UPDATE pp_userdata SET AVATAR=?,NAME=? WHERE ID=?");
        $stmt->bind_param("sss", $avatar, $name, $id);
        $stmt->execute();
        $stmt->close();
    }
}
     */

    public CompletableFuture<String> getAvatar(String id) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM pp_userdata WHERE ID=?");
        try {
            preparedStatement.setString(1, id);
            
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                try {
                    if (resultSet.next()) {
                        completableFuture.complete(resultSet.getString("AVATAR"));    
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
    
    public void updateData(String id, String avatar, String name) {
        getAvatar(id).thenAccept(s -> {
            if (s == null) {
                PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO pp_userdata (ID, AVATAR, NAME) VALUES (?, ?, ?)");
                try {
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, avatar);
                    preparedStatement.setString(3, name);
                    
                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE pp_userdata SET AVATAR=?, NAME=? WHERE ID=?");
                try {
                    preparedStatement.setString(1, avatar);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, id);

                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
