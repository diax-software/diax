package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.X
import me.diax.diax.util.Embed
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
    name = "report",
    description = "[description]",
    triggers = ["report"],
    attributes = [
        CommandAttribute(key = "category", value = "information")
    ]
)
class Report : Command {

    override fun execute(message: Message, s: String) {
        if (s.isBlank()) {
            message.channel.sendMessage(Embed.error("Please describe your error report!")).queue()
            return
        }
        WebHookUtil.report(message.jda, "```${StringUtil.stripMarkdown(s)}```\nReporter: *${message.author.name}#${message.author.discriminator}*")
        message.channel.sendMessage("$SMILE - Reported successfully, join here if you need more assistance: https://discord.gg/5sJZa2y").queue()
    }
}
