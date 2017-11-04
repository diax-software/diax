package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.X
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "report", description = "[description]", triggers = arrayOf("report"), attributes = arrayOf(CommandAttribute(key = "private")))
class Report : Command {

    override fun execute(message: Message, s: String) {
        var error = ""
        if (s.length < 20) {
            error = "Please provide more information."
        } else if (s.length > 500) {
            error = "Please try and keep your report to the point."
        }
        if (error.isNotBlank()) {
            message.channel.sendMessage("$X - $error").queue()
            return
        }
        WebHookUtil.report(message.jda, "```${StringUtil.stripMarkdown(s)}```\nReporter: *${message.author.name}#${message.author.discriminator}*")
        message.channel.sendMessage("$SMILE - Reported successfully, join here if you need more assistance: https://discord.gg/5sJZa2y").queue()
    }
}
