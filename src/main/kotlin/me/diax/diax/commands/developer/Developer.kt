package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
        name = "developer",
        triggers = ["developer"],
        attributes = [
            CommandAttribute(key = "permission", value = "developer")
        ]
)
class Developer : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("${message.author.name} is an official Diax developer.").queue()
    }
}
