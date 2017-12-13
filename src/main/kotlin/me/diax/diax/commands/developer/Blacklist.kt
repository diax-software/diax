package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.data.config.entities.Config

import me.diax.diax.util.Emote
import net.dv8tion.jda.core.entities.Message

import javax.inject.Inject

@CommandDescription(
        name = "blacklist",
        triggers = ["blacklist"],
        attributes = [
            CommandAttribute(key = "permission", value = "developer")
        ]
)
class Blacklist
@Inject constructor(private val config: Config) : Command {

    override fun execute(message: Message, s: String) {
        if (message.mentionedUsers.isEmpty()) {
            message.channel.sendMessage("${Emote.X} - Mention a user to (un)blacklist.").queue()
        }
        val id = message.mentionedUsers[0].id
        when (config.blacklist.contains(id)) {
            true -> config.blacklist.remove(id)
            false -> config.blacklist.add(id)
        }
        message.channel.sendMessage("${Emote.SMILE} - ${message.mentionedUsers[0].name} has been ${if (config.blacklist.contains(id)) "added to" else "removed from"} the blacklist.").queue()
    }
}