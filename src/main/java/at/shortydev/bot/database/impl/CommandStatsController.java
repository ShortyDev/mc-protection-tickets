package at.shortydev.bot.database.impl;

import at.shortydev.bot.DiscordBot;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class CommandStatsController {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public CompletableFuture<Integer> getAmount(String id, long time) {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        String date = FORMAT.format(new Date(time));
        try {
            PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM commandSudotagStats WHERE ID=? AND DAY=?");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, date);
            DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                try {
                    if (resultSet.next())
                        completableFuture.complete(resultSet.getInt("AMOUNT"));
                    else
                        completableFuture.complete(-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return completableFuture;
    }

    public void addCommandNow(String id) {
        getAmount(id, System.currentTimeMillis()).thenAccept(integer -> {
            String date = FORMAT.format(new Date(System.currentTimeMillis()));
            if (integer == -1) {
                try {
                    PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO commandSudotagStats (ID, DAY, AMOUNT) VALUES (?, ?, ?)");
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, date);
                    preparedStatement.setInt(3, 1);
                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE commandSudotagStats SET AMOUNT=? WHERE ID=? AND DAY=?");
                    preparedStatement.setInt(1, integer + 1);
                    preparedStatement.setString(2, id);
                    preparedStatement.setString(3, date);
                    DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
