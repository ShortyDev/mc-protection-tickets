package at.shortydev.bot.commands;

import at.shortydev.bot.commands.impl.*;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class Commands {

    private final List<Command> commands;

    public Commands(boolean registerCommands) {
        commands = new ArrayList<>();
        if (registerCommands)
            registerCommands();
    }

    public void registerCommands() {
        commands.add(new HelpCommand("help", "Displays all commands available"));
        commands.add(new AssignCommand("assign", "Assigns a ticket a category of a helper", "as"));
        commands.add(new SearchLogsCommand("searchlogs", "Search logs", "sl"));
        commands.add(new SetPrefixCommand("setprefix", "Sets a new command prefix", "sp", "botprefix"));
        commands.add(new ToggleNotifyCommand("togglenotify", "Toggle ticket notify status", "ts", "tns", "tn"));
        commands.add(new CloseCommand("close", "Close a ticket", "tc", "cl"));
        commands.add(new AddMemberCommand("addmember", "Adds a member to a channel", "am", "amtc"));
        commands.add(new ChangeTypeCommand("changetype", "Changes ticket type", "ct", "typechange"));
        commands.add(new SCloseCommand("sclose", "Sends a close info"));
        commands.add(new RemoveRoleCommand("removerole", "Remove a role from a person", "rr"));
        commands.add(new StatsCommand("stats", "Get server stats", "ss"));
        commands.add(new LoveCalculatorCommand("lovecalculater", "Calculate how much people love eachother", "lc"));
        commands.add(new RandomFactCommand("randomfact", "Some random fact", "rf"));
        commands.add(new RefreshRolesCommand("refreshroles", "Refresh roles on webinterface", "rfr", "rr", "refreshr"));
        commands.add(new PanelCommand("staffpanel", "Manage the staff panel", "staffp", "spanel"));
        commands.add(new LookupCommand("lookup", "Lookup address/domain", "lu", "ipl"));
        commands.add(new GetTimestampCommand("gettimestamp", "Get timestamp of discord id", "gts", "gettimestamp"));
    }

    public void executeCommand(TextChannel textChannel, Member member, String message, Message jdaMessage) {
        String command = message.split(" ")[0];
        message = message.replaceFirst(command, "");
        for (Command cmd : commands) {
            if (command.equalsIgnoreCase(cmd.getName()) || new ArrayList<>(Arrays.asList(cmd.getAliases())).contains(command)) {
                cmd.onCommand(textChannel, member, command, message.isEmpty() ? new String[]{} : message.substring(1).split(" "), jdaMessage);
                jdaMessage.delete().queueAfter(500L, TimeUnit.MILLISECONDS);
            }
        }
    }
}
