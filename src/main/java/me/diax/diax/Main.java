package me.diax.diax;

import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.commands.action.Hug;
import me.diax.diax.commands.fun.CSGO;
import me.diax.diax.commands.information.*;
import me.diax.diax.commands.music.*;
import me.diax.diax.commands.owner.Announce;
import me.diax.diax.listeners.DisconnectListener;
import me.diax.diax.listeners.GuildJoinLeaveListener;
import me.diax.diax.listeners.MessageListener;
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

public class Main {

    public static void main(String[] args) {
        new Main().main(args[0], args[1], args[2]);
    }

    public void main(String token, String auth, String prefix) {
        try {
            CommandHandler handler = new CommandHandler();
            handler.registerCommands(
                    new Hug(),

                    new CSGO(),

                    new Credits(),
                    new Help(handler, prefix),
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

                    new Announce()
            );
            new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAudioEnabled(true)
                    .setGame(Game.of("Diax is starting, hold tight!"))
                    .setStatus(OnlineStatus.IDLE)
                    .addEventListener(
                            new DisconnectListener(),
                            new GuildJoinLeaveListener(auth),
                            new MessageListener(handler, prefix),
                            new ListenerAdapter() {
                                @Override
                                public void onReady(ReadyEvent event) {
                                    JDA jda = event.getJDA();
                                    WebHookUtil.log(jda, Emote.SPARKLES + " Start", jda.getSelfUser().getName() + " has finished starting!");
                                    try {
                                        JDAUtil.sendGuilds(jda, auth);
                                    } catch (Exception ignored) {
                                    }
                                    JDAUtil.startGameChanging(jda, prefix);
                                }
                            }
                    ).buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}