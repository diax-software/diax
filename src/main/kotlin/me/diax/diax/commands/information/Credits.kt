package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "credits", triggers = arrayOf("credits"), attributes = arrayOf(CommandAttribute(key = "private")))
class Credits : Command {

    override fun execute(message: Message, s: String) {
        message.textChannel.sendMessage(
                Embed.transparent()
                        .addField("comportment#4475", "Owner // Head-Developer", false)
                        .addField("AdrianTodt#0722", "Co-Owner // Backend-Develoepr", false)
                        .addField("Coolguy3289#2290", "Co-Owner // Website-Developer", false)
                        .build()
        ).queue()
    }
}