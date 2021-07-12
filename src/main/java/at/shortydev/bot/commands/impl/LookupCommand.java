package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;

public class LookupCommand extends Command {

    public LookupCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @SneakyThrows
    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            return;
        }
        if (args.length != 1) {
            return;
        }
        URL url = new URL("http://ip-api.com/json/" + args[0]);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = inputStreamReader.readLine();
        if (line == null) {
            line = "err";
        }
        try {
            JSONObject jsonObject = new JSONObject(line);
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                EasyEmbed.builder()
                        .title("Lookup results")
                        .description("Lookup successful for **" + args[0] + "**")
                        .color(Color.GREEN)
                        .autoDelete(20)
                        .footer(EasyEmbed.Footer.builder().text(DiscordBot.publicFooter).build())
                        .timestamp(Instant.now())
                        .fields(new ArrayList<>())
                        .build()
                        .addField(new MessageEmbed.Field("Country", jsonObject.getString("country") + " (" + jsonObject.getString("countryCode") + ")", true))
                        .addField(new MessageEmbed.Field("Region", jsonObject.getString("regionName") + " (lat.: " + jsonObject.getString("lat") + " lon.: " + jsonObject.getString("lon") + ")", true))
                        .addField(new MessageEmbed.Field("Timezone", jsonObject.getString("timezone"), false))
                        .addField(new MessageEmbed.Field("ISP", jsonObject.getString("isp"), false))
                        .addField(new MessageEmbed.Field("Organisation", jsonObject.getString("org"), true))
                        .addField(new MessageEmbed.Field("Address", jsonObject.getString("query"), false))
                        .buildMessageAndSend(channel);
            } else {
                EasyEmbed.builder()
                        .title("Error while getting information")
                        .color(Color.RED)
                        .build()
                        .buildMessageAndSend(channel);
            }
        } catch (JSONException err) {

        }
    }
}
