package me.diax.diax.util;

import com.github.natanbc.discordbotsapi.DiscordBotsAPI;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JDAUtil {

    private static DiscordBotsAPI API;

    private static String getStatus(JDA jda) {
        String[] status = new String[]{
                "Guilds: (guilds) | Users: (users)",
                "Invite: https://discord.gg/5sJZa2y",
                "Donate: https://patreon.com/comportment"
        };
        return "help | " + status[new Random().nextInt(status.length)]
                .replaceAll("\\(guilds\\)", jda.getGuilds().size() + "")
                .replaceAll("\\(users\\)", jda.getUsers().size() + "");
    }

    public static void startGameChanging(JDA jda, String prefix) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable periodicTask = () -> {
            String status = jda.getPresence().getGame().getName();
            while (status.equals(jda.getPresence().getGame().getName())) {
                status = JDAUtil.getStatus(jda);
            }
            jda.getPresence().setGame(Game.of(prefix + status));
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
        };
        executor.scheduleAtFixedRate(periodicTask, 0, 20, TimeUnit.SECONDS);
    }

    public static void sendGuilds(JDA jda, String token) {
        if (token.isEmpty()) return;
        if (API == null) API = new DiscordBotsAPI(token);
        try {
            API.postStats(1, 0, jda.getGuilds().size());
        } catch (Exception e) {
            WebHookUtil.err(jda, "Couldn't update bot list stats.");
        }
    }
}