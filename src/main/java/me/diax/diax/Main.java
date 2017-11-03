package me.diax.diax;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.injection.DiaxInjections;
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
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

@Slf4j
public class Main {

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
            log.error("Couldn't load data file.");
            e.printStackTrace();
            System.exit(1);
        }
        requester = new WeebAPI(data.getWeebToken());
        try {
            //todo maybe move code to a separated class

            Reflections reflections = new Reflections("me.diax.diax");
            CommandHandler handler = new CommandHandler();

            Injector injector = new DiaxInjections(handler, data, requester).toInjector();

            //Welcome to automation
            handler.registerCommands(
                reflections.getSubTypesOf(Command.class).stream()
                    .filter(c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(CommandDescription.class))
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