package me.diax.diax.commands.information

import com.sedmelluq.discord.lavaplayer.tools.PlayerLibrary
import me.diax.comportment.jdacommand.*
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.JDAInfo
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(
    name = "info",
    triggers = ["info", "stats", "status", "statistics", "information"],
    attributes = [
        CommandAttribute(key = "category", value = "information")
    ]
)
class Info
@Inject constructor(private val handler: CommandHandler) : Command {

    override fun execute(message: Message, s: String) {
        val jda = message.jda
        val runtime = Runtime.getRuntime()
        val java = System.getProperty("java.runtime.version")
        message.channel.sendMessage(Embed.transparent().setDescription(arrayOf(
            "```prolog",
            "----- Library Versions -----",
            "",
            "JDA: ${JDAInfo.VERSION}",
            "JDA-Command: ${JDACommandInfo.VERSION}",
            "Discord API: v${JDAInfo.DISCORD_REST_VERSION}",
            "Lavaplayer: ${PlayerLibrary.VERSION}",
            "",
            "----- Diax Information -----",
            "",
            "API Ping: ${jda.ping}ms",
            "Guilds: ${jda.guilds.size}",
            "Text Channels: ${jda.textChannels.size}",
            "Users: ${jda.users.size}",
            "Commands: ${handler.commands.size}",
            "Owner: comportment#4475",
            "",
            "----- VPS Information -----",
            "",
            "Location: ${System.getProperty("user.country")}",
            "Java Version: ${if (java.contains("-")) java.split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0] else java}",
            "OS: ${"${System.getProperty("os.name")}-${System.getProperty("os.version")}"}",
            "OS Arch: ${System.getProperty("os.arch")}",
            "RAM: ${(runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024}Mb/${runtime.totalMemory() / 1024 / 1024}Mb",
            "RAM Usage: ${(runtime.totalMemory() - runtime.freeMemory()) / runtime.totalMemory() * 100}%",
            "Threads: ${Thread.getAllStackTraces().keys.size}",
            "```").joinToString("\n")).build()).queue()

    }
}
