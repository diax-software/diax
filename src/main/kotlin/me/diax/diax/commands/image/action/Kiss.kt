package me.diax.diax.commands.image.action

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.HEART
import me.diax.diax.util.Emote.X
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.WeebAPI
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message
import java.util.stream.Collectors
import javax.inject.Inject

@CommandDescription(
    name = "kiss",
    triggers = ["kiss"]
)
class Kiss
@Inject constructor(private val requester: WeebAPI) : Command {

    override fun execute(message: Message, s: String) {
        var image: String
        try {
            image = requester.getRandomImageByType("kiss", WeebAPI.NSFW.FALSE, "gif")!!
        } catch (e: Exception) {
            image = ""
            WebHookUtil.err(message.jda, "Error retrieving weeb.sh image!")
        }

        if (image.isBlank()) {
            message.channel.sendMessage("$X - Something went wrong with fetching the image!").queue()
            return
        }
        if (message.mentionedUsers.isEmpty()) {
            message.channel.sendMessage("$X - Please @mention somebody to kiss.").queue()
            return
        }
        if (message.mentionedUsers.size > 1) {
            message.channel.sendMessage("$X - You can't kiss more than one person at a time you baka-lewd!").queue()
            return
        }
        val msg = "***$HEART - ${if (message.mentionedUsers.contains(message.author)) "Diax " else StringUtil.stripMarkdown(message.member.effectiveName)} is kissing ${StringUtil.stripMarkdown(message.mentionedUsers.stream().map { i -> i.name }.collect(Collectors.joining(", ")))}***"
        message.channel.sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue()
    }
}