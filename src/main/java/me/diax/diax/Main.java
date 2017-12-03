package me.diax.diax;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.data.config.ConfigManager;
import me.diax.diax.injection.DiaxInjections;
import me.diax.diax.listeners.GuildJoinLeaveListener;
import me.diax.diax.listeners.MessageListener;
import me.diax.diax.util.DiscordLogBack;
import me.diax.diax.util.Emote;
import me.diax.diax.util.JDAUtil;
import me.diax.diax.util.WebHookUtil;
import me.diax.diax.util.style.BotType;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.stream.Collectors;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        ConfigManager manager = new ConfigManager();
        Runtime.getRuntime().addShutdownHook(new Thread(manager::save));

        try {
            manager.load();
        } catch (Exception e) {
            manager.save();
            log.error("Couldn't load data file. Please load it again");
            e.printStackTrace();
            System.exit(1);
        }

        manager.save();

        BotType.CURRENT_TYPE = BotType.valueOf(manager.get().getType().toUpperCase());

        //todo maybe move code to a separated class

        Reflections reflections = new Reflections("me.diax.diax");
        CommandHandler handler = new CommandHandler();

        Injector injector = new DiaxInjections(handler, manager).toInjector();

        //Welcome to automation
        handler.registerCommands(
                reflections.getSubTypesOf(Command.class).stream()
                        .filter(c -> !Modifier.isAbstract(c.getModifiers()) && c.isAnnotationPresent(CommandDescription.class))
                        .map(injector::getInstance)
                        .collect(Collectors.toSet())
        );

        new JDABuilder(AccountType.BOT)
                .setToken(manager.get().getTokens().getDiscord())
                .setAudioEnabled(true)
                .setGame(Game.of("Diax is starting, hold tight!"))
                .setStatus(OnlineStatus.IDLE)
                .addEventListener(
                        new GuildJoinLeaveListener(manager.get().getTokens().getBotlist()),
                        new MessageListener(handler, manager.get()),
                        new ListenerAdapter() {
                            @Override
                            public void onReady(ReadyEvent event) {
                                DiscordLogBack.enable(event.getJDA().getTextChannelById(manager.get().getChannels().getOutput()));
                                JDA jda = event.getJDA();
                                WebHookUtil.log(jda, Emote.SPARKLES + " Start", "Diax has finished starting!");
                                JDAUtil.startGameChanging(jda, manager.get().getPrefix());
                                JDAUtil.sendGuilds(event.getJDA(), manager.get().getTokens().getBotlist());
                            }
                        }
                ).buildBlocking();
    }
}