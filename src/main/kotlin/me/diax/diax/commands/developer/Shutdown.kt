package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Data
import me.diax.diax.util.Emote
import me.diax.diax.util.Emote.X
import me.diax.diax.util.Emote.ZZZ
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(name = "shutdown", triggers = arrayOf("shutdown"), attributes = arrayOf(CommandAttribute(key = "developer"), CommandAttribute(key = "hidden")))
class Shutdown @Inject
constructor(private val data: Data) : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage(Emote.ZZZ + "$ZZZ - Shutting down...").queue()
        data.addDeveloper("101")
        try {
            data.saveData()
        } catch (e: Exception) {
            message.channel.sendMessage(Emote.X + "$X - Error saving data.").queue()
        }
        System.exit(-1)
    }
}