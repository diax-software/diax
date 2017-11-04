package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.util.Embed
import net.dv8tion.jda.core.entities.Message
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Named

@CommandDescription(name = "help", triggers = arrayOf("help", "commands"), attributes = arrayOf(CommandAttribute(key = "private")))
class Help @Inject
constructor(private val handler: CommandHandler, @param:Named("prefix") private val prefix: String) : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage(Embed.transparent()
                .addField("__**Commands**__",
                        arrayOf(
                                "Optional arguments are `{}`",
                                "Needed arguments are `[]`",
                                handler.commands.stream().filter { cmd -> !cmd.hasAttribute("hidden") }.sorted().map { cmd -> "`$prefix${cmd.description.name} ${if (cmd.description.description.isBlank()) "" else "| ${cmd.description.description}"}`" }.collect(Collectors.joining("\n"))
                        ).joinToString("\n"),
                        false)
                .addField("__**Links**__", arrayOf(
                        "[Invite](https://discordapp.com/oauth2/authorize?scope=bot&client_id=295500621862404097&permissions=3198016) Invite me!",
                        "[Patreon](https://patreon.com/comportment) - Donate here to help support us!",
                        "[Discord](https://discord.gg/5sJZa2y) - Come here to chat or for help!",
                        "[Website](http://diax.me) - Check out our website!",
                        "[Upvote](https://discordbots.org/bot/295500621862404097) - Upvote me on DiscordBots!").joinToString("\n"), false
                ).build()).queue()
    }
}