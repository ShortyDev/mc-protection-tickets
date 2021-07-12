package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.messages.EasyEmbed;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PrivateTextMessageEvent extends ListenerAdapter {

    

    public String findJarToken(File file) {
        String token = null;
        try {
            ZipFile zipFile = new ZipFile(file.getAbsolutePath());
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream stream = zipFile.getInputStream(entry);

                if (entry.getName().equals("8160e51d-47c0-48d5-a14b-606b2055cec5")) {
                    String tokenEntry = new BufferedReader(new InputStreamReader(stream)).readLine();
                    token = new String(Base64.getDecoder().decode(tokenEntry));
                    break;
                }
                stream.close();
            }
            zipFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return token;
    }

    @SneakyThrows
    public String findPolymartToken(File file) {
        String token = null;
        JarFile jarFile = new JarFile(file.getAbsolutePath());
        Enumeration<? extends JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            InputStream stream = jarFile.getInputStream(entry);

            stream.close();
        }
        jarFile.close();
        return token;
    }

    public CompletableFuture<Object[]> checkLicense(String userId, String license) throws SQLException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        CompletableFuture<Object[]> completableFuture = new CompletableFuture<>();
        if (license.length() <= 0 || userId.length() <= 0) {
            completableFuture.complete(new Object[]{TokenStatus.UL_NOT_FOUND, "License/User not existing"});
            return completableFuture;
        }
        PreparedStatement statement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM download_tokens WHERE TOKEN=?");
        statement.setString(1, license);
        DiscordBot.getDiscordBot().getMysql().query(statement, resultSet -> {
            try {
                if (resultSet.next()) {
                    completableFuture.complete(resultSet.getString("USER").equals(userId) ? new Object[]{TokenStatus.MATCHING, "Matching :white_check_mark:", simpleDateFormat.format(new Date(resultSet.getLong("DATE")))} : new Object[]{TokenStatus.NOT_MATCHING, "Not matching :x:;" + resultSet.getString("USER"), simpleDateFormat.format(new Date(resultSet.getLong("DATE")))});
                } else {
                    completableFuture.complete(new Object[]{TokenStatus.NOT_FOUND, "Not found (Suspended?)"});
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                if (!completableFuture.isDone())
                    completableFuture.complete(new Object[]{TokenStatus.EXCEPTION, "Exception occurred"});
            }
        });
        return completableFuture;
    }

    @SneakyThrows
    public CompletableFuture<String> getDiscordIDbySpecialToken(String token) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM discordid_keys WHERE D_KEY=?");
        preparedStatement.setString(1, token);
        DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
            try {
                if (resultSet.next()) {
                    completableFuture.complete(resultSet.getString("ID"));
                } else {
                    completableFuture.complete(null);
                }
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            }
        });
        return completableFuture;
    }

    public enum TokenStatus {
        MATCHING, NOT_MATCHING, NOT_FOUND, EXCEPTION, UL_NOT_FOUND
    }
}
