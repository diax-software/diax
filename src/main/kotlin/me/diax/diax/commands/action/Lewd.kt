package me.diax.diax.commands.action

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import me.diax.diax.util.Emote.X
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.WeebAPI
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(name = "lewd", triggers = arrayOf("lewd"))
class Lewd @Inject
constructor(private val requester: WeebAPI) : Command {

    override fun execute(message: Message, s: String) {
        var image: String
        try {
            image = requester.getRandomImageByType("lewd", WeebAPI.NSFW.FALSE)
        } catch (e: Exception) {
            image = ""
            WebHookUtil.err(message.jda, "Error retrieving weeb.sh image!")
        }

        if (image.isBlank()) {
            message.channel.sendMessage("$X - Something went wrong with fetching the image!").queue()
            return
        }
        message.channel.sendMessage(Embed.transparent().setDescription("***Too lewd!***").setImage(image).build()).queue()
    }
}