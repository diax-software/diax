package me.diax.diax.commands.information;

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary;
import me.diax.comportment.jdacommand.*;
import me.diax.diax.util.Embed;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.Message;

import javax.inject.Inject;

@CommandDescription(
        name = "info",
        triggers = {
                "info", "stats", "status", "statistics", "information"
        },
        attributes = {
                @CommandAttribute(key = "private")
        }
)
public class Info implements Command {

    private CommandHandler handler;

    @Inject
    public Info(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(Message message, String s) {
        JDA jda = message.getJDA();
        Runtime runtime = Runtime.getRuntime();
        message.getChannel().sendMessage(Embed.transparent().setDescription(String.join("\n",
                "```prolog",
                "----- Library Versions -----",
                "",
                "JDA: " + JDAInfo.VERSION,
                "JDA-Command: " + JDACommandInfo.VERSION,
                "Discord API: v" + JDAInfo.DISCORD_REST_VERSION,
                "Lavaplayer: " + PlayerLibrary.VERSION,
                "",
                "----- Diax Information -----",
                "",
                "API Ping: " + jda.getPing(),
                "Guilds: " + jda.getGuilds().size(),
                "Text Channels: " + jda.getTextChannels().size(),
                "Users: " + jda.getUsers().size(),
                "Commands: " + handler.getCommands().size(),
                "Owner: comportment#4475",
                "",
                "----- VPS Information -----",
                "",
                "Location: " + System.getProperty("user.country"),
                "Java Version: " + System.getProperty("java.runtime.version"),
                "OS: " + System.getProperty("os.name") + "-" + System.getProperty("os.version"),
                "OS Arch: " + System.getProperty("os.arch"),
                "RAM: " + ((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024) + "Mb/" + (runtime.totalMemory() / 1024 / 1024) + "Mb",
                "RAM Usage: " + (((runtime.totalMemory() - runtime.freeMemory()) / runtime.totalMemory()) * 100) + "%",
                "Threads: " + Thread.getAllStackTraces().keySet().size(),
                "```"
        )).build()).queue();

    }
}
