package me.diax.diax.util;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JDAUtil {

    private static String getStatus(JDA jda) {
        String[] status = new String[]{
                "Guilds: (guilds) | Users: (users)",
                "Invite: https://discord.gg/5sJZa2y",
                "Donate: https://patreon.com/comportment"
        };
        return "<>help | " + status[new Random().nextInt(status.length)]
                .replaceAll("\\(guilds\\)", jda.getGuilds().size() + "")
                .replaceAll("\\(users\\)", jda.getUsers().size() + "");
    }

    @Deprecated
    public static void updateGuilds(JDA jda) {
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setGame(Game.of("<>help | Guilds: " + jda.getGuilds().size()));
    }

    public static void startGameChanging(JDA jda) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask = () -> {
            String status = jda.getPresence().getGame().getName();
            while (status.equals(jda.getPresence().getGame().getName())) {
                status = getStatus(jda);
            }
            jda.getPresence().setGame(Game.of(status));
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        };
        executor.scheduleAtFixedRate(periodicTask, 0, 20, TimeUnit.SECONDS);
    }

    @Deprecated
    public static void log(JDA jda, String message) {
        jda.getTextChannelById("357109761533149185").sendMessage(message).queue();
    }
}