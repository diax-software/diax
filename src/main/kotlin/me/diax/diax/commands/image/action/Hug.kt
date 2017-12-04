package me.diax.diax.commands.image.action

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.X
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.WeebAPI
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.inject.Inject

@CommandDescription(
    name = "hug",
    triggers = ["hug"]
)
class Hug
@Inject constructor(private val requester: WeebAPI) : Command {

    override fun execute(message: Message, s: String) {
        var image: String
        try {
            image = requester.getRandomImageByType("hug", WeebAPI.NSFW.FALSE, "gif")!!
        } catch (e: Exception) {
            image = ""
            WebHookUtil.err(message.jda, "Error retrieving weeb.sh image!")
        }

        if (image.isBlank()) {
            message.channel.sendMessage("$X - Something went wrong with fetching the image!").queue()
            return
        }
        if (message.mentionedUsers.isEmpty()) {
            message.channel.sendMessage("$X - Please @mention somebody to hug.").queue()
            return
        }
        if (message.mentionedUsers.size > 10) {
            message.channel.sendMessage("$X - You are trying to hug too many people!").queue()
            return
        }
        var msg = "***$SMILE - ${if (message.mentionedUsers.contains(message.author)) "Diax " else StringUtil.stripMarkdown(message.member.effectiveName)} is hugging ${StringUtil.stripMarkdown(message.mentionedUsers.stream().map { i -> i.name }.collect(Collectors.joining(", ")))}***"
        if (message.mentionedUsers.size > 1) {
            val last = message.mentionedUsers.last().name
            msg = StringUtil.replaceLast(msg, Pattern.quote(", " + last), " and " + last)
        }
        message.channel.sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue()
    }
}