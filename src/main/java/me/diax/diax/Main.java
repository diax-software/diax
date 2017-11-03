package me.diax.diax;

import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.commands.action.Hug;
import me.diax.diax.commands.action.Kiss;
import me.diax.diax.commands.action.Lewd;
import me.diax.diax.commands.action.Pat;
import me.diax.diax.commands.developer.*;
import me.diax.diax.commands.developer.Shutdown;
import me.diax.diax.commands.fun.*;
import me.diax.diax.commands.image.Catgirl;
import me.diax.diax.commands.information.*;
import me.diax.diax.commands.music.*;
import me.diax.diax.listeners.GuildJoinLeaveListener;
import me.diax.diax.listeners.MessageListener;
import me.diax.diax.util.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private Data data;
    private WeebAPI requester;

    public static void main(String[] args) {
        new Main().main(args.length == 0 || args[0].isEmpty() ? System.getProperty("user.dir") + "/data.json" : args[0]);
    }

    public void main(String location) {
        try {
            data = new Data(new File(location));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> data.saveData()));
        } catch (Exception e) {
            logger.error("Couldn't load data file.");
            e.printStackTrace();
            System.exit(1);
        }
        requester = new WeebAPI(data.getWeebToken());
        try {
            CommandHandler handler = new CommandHandler();
            handler.registerCommands(
                    new Hug(requester),
                    new Kiss(requester),
                    new Lewd(requester),
                    new Pat(requester),

                    new CSGO(),
                    new Die(),
                    new EightBall(),
                    new Flip(),
                    new Say(),

                    new Catgirl(),

                    new Credits(),
                    new Help(handler, data.getPrefix()),
                    new Info(handler),
                    new Invite(),
                    new Links(),
                    new Ping(),
                    new Report(),
                    new Suggest(),

                    new Join(),
                    new NowPlaying(),
                    new Play(),
                    new Queue(),
                    new Repeat(),
                    new Shuffle(),
                    new Skip(),
                    new Stop(),
                    new Volume(),

                    new Announce(),
                    new Blacklist(data),
                    new Developer(),
                    new Reload(data),
                    new Save(data),
                    new Shutdown(data)
            );
            new JDABuilder(AccountType.BOT)
                    .setToken(data.getToken())
                    .setAudioEnabled(true)
                    .setGame(Game.of("Diax is starting, hold tight!"))
                    .setStatus(OnlineStatus.IDLE)
                    .addEventListener(
                            new GuildJoinLeaveListener(data.getBotlistToken()),
                            new MessageListener(handler, data),
                            new ListenerAdapter() {
                                @Override
                                public void onReady(ReadyEvent event) {
                                    JDA jda = event.getJDA();
                                    WebHookUtil.log(jda, Emote.SPARKLES + " Start", jda.getSelfUser().getName() + " has finished starting!");
                                    JDAUtil.startGameChanging(jda, data.getPrefix());
                                    JDAUtil.sendGuilds(event.getJDA(), data.getBotlistToken());
                                }
                            }
                    ).buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}