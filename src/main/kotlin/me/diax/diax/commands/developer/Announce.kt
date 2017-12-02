package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.WebHookUtil
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
    name = "announce",
    triggers = ["announce"],
    attributes = [
        CommandAttribute(key = "category", value = "developer")
    ]
)
class Announce : Command {
    override fun execute(message: Message, s: String) {
        WebHookUtil.announce(message.jda, "$s - *${message.author.name}*")
    }
}