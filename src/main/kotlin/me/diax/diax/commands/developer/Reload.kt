package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(name = "reload", triggers = arrayOf("reload"),
        attributes = arrayOf(CommandAttribute(key = "category", value = "developer")))
class Reload @Inject
constructor(private val configManager: ConfigManager) : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("$SMILE - Reloading data...").queue()
        try {
            configManager.load()
        } catch (e: Exception) {
            message.channel.sendMessage("$X - Error reloading data!").queue()
        }
    }
}