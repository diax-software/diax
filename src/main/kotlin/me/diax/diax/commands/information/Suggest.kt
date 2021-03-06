package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
        name = "suggest",
        description = "[description]",
        triggers = ["suggest"]
)
class Suggest : Command {

    override fun execute(message: Message, s: String) {
        if (s.isBlank()) {
            message.channel.sendMessage(Embed.error("Please describe your suggestion!")).queue()
            return
        }
        WebHookUtil.suggest(message.jda, "```${StringUtil.stripMarkdown(s)}```\n*Suggested by ${StringUtil.stripMarkdown(message.author.name)}#${message.author.discriminator}*")
        message.channel.sendMessage("$SMILE - Your suggestion has been submitted, join here to track it: https://discord.gg/5sJZa2y").queue()
    }
}
