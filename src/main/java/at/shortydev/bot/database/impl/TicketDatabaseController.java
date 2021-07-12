package at.shortydev.bot.database.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.settings.ServerSettings;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TicketDatabaseController {

    public void insert(String id, String message) {
        if (message.startsWith(".sl") || message.startsWith("searchlogs")) {
            return;
        }
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO ticketLogs(ID, TIME, MESSAGE) VALUES (?, ?, ?)");
        try {
            preparedStatement.setString(1, id);
            preparedStatement.setLong(2, System.currentTimeMillis());
            preparedStatement.setString(3, message);
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @SneakyThrows
    public CompletableFuture<String[]> findMessages(String query) {
        CompletableFuture<String[]> completableFuture = new CompletableFuture<>();

        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM ticketLogs WHERE MESSAGE LIKE ?");
        preparedStatement.setString(1, "%" + query + "%");
        DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
            try {
                List<String> messages = new ArrayList<>();
                while (resultSet.next()) {
                    messages.add(resultSet.getString("ID") + "%__$__%" + resultSet.getLong("TIME") + "%__$__%" + resultSet.getString("MESSAGE"));
                }
                completableFuture.complete(messages.toArray(new String[]{}));
            } catch (Exception ignored) {
            }
        });
        return completableFuture;
    }
}
