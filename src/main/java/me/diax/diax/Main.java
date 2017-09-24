package me.diax.diax;

import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.commands.action.Hug;
import me.diax.diax.commands.fun.CSGO;
import me.diax.diax.commands.information.Credits;
import me.diax.diax.commands.information.Help;
import me.diax.diax.commands.information.Ping;
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

public class Main {

    public static void main(String[] args) {
        new Main().main(args[0]);
    }

    public void main(String token) {
        try {
            CommandHandler handler = new CommandHandler();
            handler.registerCommands(
                    new Hug(),

                    new CSGO(),

                    new Credits(),
                    new Help(handler),
                    new Ping(),

                    new Join(),
                    new NowPlaying(),
                    new Play(),
                    new Repeat(),
                    new Shuffle(),
                    new Skip(),
                    new Stop(),
                    new Volume(),

                    new Announce()
            );
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setAudioEnabled(true)
                    .setGame(Game.of("Diax is starting, hold tight!"))
                    .setStatus(OnlineStatus.IDLE)
                    .addEventListener(
                            new DisconnectListener(),
                            new GuildJoinLeaveListener(),
                            new MessageListener(handler)
                    ).buildBlocking();
            WebHookUtil.log(jda, Emote.SPARKLES + " Start", "Diax has finished starting!");
            JDAUtil.startGameChanging(jda);
        } catch (Exception ignored) {}
    }
}