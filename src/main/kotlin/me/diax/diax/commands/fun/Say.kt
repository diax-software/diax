package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "say", triggers = arrayOf("say"), description = "[message]",
        attributes = arrayOf(CommandAttribute(key = "category", value = "fun")))
class Say : Command {

    override fun execute(message: Message, s: String) {
        if (s.isEmpty()) {
            message.channel.sendMessage("$X - Specify something to say.").queue()
        } else {
            message.channel.sendMessage(s).queue()
        }
    }
}