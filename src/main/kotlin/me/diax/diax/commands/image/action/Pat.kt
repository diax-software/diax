package me.diax.diax.commands.image.action

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import me.diax.diax.util.Emote.X
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.WeebAPI
import net.dv8tion.jda.core.entities.Message
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.inject.Inject

@CommandDescription(
    name = "pat",
    triggers = ["pat"],
    attributes = [
        CommandAttribute(key = "category", value = "action")
    ]
)
class Pat
@Inject constructor(private val requester: WeebAPI) : Command {

    override fun execute(message: Message, s: String) {
        var image: String
        try {
            image = requester.getRandomImageByType("pat", WeebAPI.NSFW.FALSE, "gif")
        } catch (e: Exception) {
            image = ""
            WebHookUtil.err(message.jda, "Error retrieving weeb.sh image!")
            e.printStackTrace()
        }

        if (image.isBlank()) {
            message.channel.sendMessage("$X - Something went wrong with fetching the image!").queue()
            return
        }
        if (message.mentionedUsers.isEmpty()) {
            message.channel.sendMessage("$X - Please @mention somebody to pat.").queue()
            return
        }
        if (message.mentionedUsers.size > 10) {
            message.channel.sendMessage("$X - You are trying to pat too many people!").queue()
            return
        }
        var msg = "***${if (message.mentionedUsers.contains(message.author)) "Diax " else StringUtil.stripMarkdown(message.member.effectiveName)} is patting ${StringUtil.stripMarkdown(message.mentionedUsers.stream().map { i -> i.name }.collect(Collectors.joining(", ")))}***"
        if (message.mentionedUsers.size > 1) {
            val last = message.mentionedUsers[message.mentionedUsers.size - 1].name
            msg = StringUtil.replaceLast(msg, Pattern.quote(", " + last), " and " + last)
        }
        message.channel.sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue()
    }
}