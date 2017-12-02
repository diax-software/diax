package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message
import javax.inject.Inject

@CommandDescription(
    name = "save",
    triggers = ["save"],
    attributes = [
        CommandAttribute(key = "category", value = "developer"),
        CommandAttribute(key = "developer")
    ]
)
class Save
@Inject constructor(private val configManager: ConfigManager) : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("$SMILE - Saving data....").queue()
        try {
            configManager.save()
        } catch (e: Exception) {
            message.channel.sendMessage("$X - Error saving data.").queue()
        }
    }
}