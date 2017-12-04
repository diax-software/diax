package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.util.Emote.X
import me.diax.diax.util.Emote.ZZZ
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(
    name = "shutdown",
    triggers = ["shutdown"],
    attributes = [

        CommandAttribute(key = "permission", value = "developer")
    ]
)
class Shutdown
@Inject constructor(private val configManager: ConfigManager) : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("$ZZZ - Shutting down...").queue()
        try {
            configManager.save()
        } catch (e: Exception) {
            message.channel.sendMessage("$X - Error saving data.").queue()
        }
        System.exit(-1)
    }
}