package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "developer", triggers = arrayOf("developer"), attributes = arrayOf(CommandAttribute(key = "developer"), CommandAttribute(key = "hidden")))
class Developer : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("${message.author.name} is an official Diax developer.").queue()
    }
}
