package me.diax.diax;

import com.google.inject.Injector;
import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.injection.DiaxInjections;
import me.diax.diax.listeners.GuildJoinLeaveListener;
import me.diax.diax.listeners.MessageListener;
import me.diax.diax.util.Data;
import me.diax.diax.util.Emote;
import me.diax.diax.util.JDAUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        new Main().main(args.length == 0 || args[0].isEmpty() ? System.getProperty("user.dir") + "/data.json" : args[0]);
    }

    private Data data;

    public void main(String location) {
        try {
            data = new Data(new File(location));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> data.saveData()));
        } catch (Exception e) {
            logger.error("Couldn't load data file.");
            e.printStackTrace();
            System.exit(1);
        }
        try {
            //todo maybe move code to a separated class

            Reflections reflections = new Reflections("me.diax.diax");
            CommandHandler handler = new CommandHandler();

            Injector injector = new DiaxInjections(handler, data).toInjector();

            //Welcome to automation
            handler.registerCommands(
                reflections.getSubTypesOf(Command.class).stream()
                    .filter(c -> Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(CommandDescription.class))
                    .map(injector::getInstance)
                    .collect(Collectors.toSet())
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