package at.shortydev.bot;

import at.shortydev.bot.commands.Commands;
import at.shortydev.bot.database.AsyncMySQL;
import at.shortydev.bot.database.impl.*;
import at.shortydev.bot.listeners.*;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.SQLException;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class DiscordBot {

    @Getter
    private static DiscordBot discordBot;

    private ShardManager shardManager;
    private AsyncMySQL mysql;

    private ServerDatabaseController serverDatabaseController;
    private RoleDatabaseController roleDatabaseController;
    private TicketDatabaseController ticketDatabaseController;
    private CommandStatsController commandStatsController;
    private PanelDatabaseController panelDatabaseController;
    private MemberStatsController memberStatsController;
    private Commands commands;
    private TicketListener ticketListener;

    public static final String publicFooter = "mc-protection bot | by Shorty#8274";
    public static final ExecutorService executor = Executors.newFixedThreadPool(2);

    @SneakyThrows
    public void enable(String[] args) {
        discordBot = this;

        if (args.length < 3) {
            System.out.println("########## bot-token and mysql-credentials and cloudflareapi are missing ##########");
            System.exit(-1);
            return;
        }
        System.out.println();
        String connectionString = args[1];
        String cloudflare = args[2];
        String[] connectionArguments = connectionString.split(":");
        mysql = new AsyncMySQL(connectionArguments[0], Integer.parseInt(connectionArguments[1]), connectionArguments[2], connectionArguments[3], connectionArguments[4]);
        createTables();
   
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(args[0]);
        builder.addEventListeners(new TextMessageEvent());
        builder.addEventListeners(new GuildJoinEvent());
        builder.addEventListeners(new GuildLeaveEvent());
        builder.addEventListeners(new MessageReactEvent());
        builder.addEventListeners(new GuildMemberRoleAddEvent());
        builder.addEventListeners(new GuildMemberRoleRemoveEvent());
        builder.addEventListeners(new PrivateTextMessageEvent());
        builder.addEventListeners(new RoleUpdateEvent());
        builder.addEventListeners(new GuildMemberJoinLeaveEvent());
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        shardManager = builder.build();
        shardManager.setStatus(OnlineStatus.DO_NOT_DISTURB);

        this.serverDatabaseController = new ServerDatabaseController();
        this.roleDatabaseController = new RoleDatabaseController();
        this.ticketDatabaseController = new TicketDatabaseController();
        this.commandStatsController = new CommandStatsController();
        this.panelDatabaseController = new PanelDatabaseController();
        this.memberStatsController = new MemberStatsController();
        this.commands = new Commands(true);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ticketListener = new TicketListener();
                ticketListener.start();
            }
        };
        executor.execute(timerTask);
    }

    private void createTables() {
        try {
            if (!mysql.getMySQL().getConnection().isClosed()) {
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS b_servers(ID TEXT, commandsUsed INT, botJoined LONG, PREFIX TEXT)"));
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS dc_roles(ID TEXT, ROLES TEXT)"));
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS ticketLogs(ID TEXT, TIME TEXT, MESSAGE TEXT)"));
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS commandSudotagStats(ID VARCHAR(100), DAY VARCHAR(100), AMOUNT INT)"));
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS memberActions(ACTION VARCHAR(100), ID VARCHAR(100), DAY VARCHAR(100), MILLIS VARCHAR(100), NEW_AMOUNT VARCHAR(100))"));
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS messageLogs(ID VARCHAR(100), DAY VARCHAR(100), MILLIS VARCHAR(100), MESSAGE TEXT, CHANNEL_ID VARCHAR(100), CHANNEL_NAME TEXT)"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean isValidPrefix(String s) {
        return s != null && s.matches("^[a-zA-Z0-9!@#$&()-`.+,/\"]*$");
    }

    public static boolean isTicketChannel(String s) {
        return s != null && s.matches("^[a-z]{2}-ticket-\\d{1,5}");
    }
}
