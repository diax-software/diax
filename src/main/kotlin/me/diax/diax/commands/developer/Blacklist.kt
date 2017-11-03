package me.diax.diax.commands.developer

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Data
import me.diax.diax.util.Emote
import net.dv8tion.jda.core.entities.Message

import javax.inject.Inject

@CommandDescription(name = "blacklist", triggers = arrayOf("blacklist"), attributes = arrayOf(CommandAttribute(key = "developer"), CommandAttribute(key = "hidden")))
class Blacklist @Inject
constructor(private val data: Data) : Command {

    override fun execute(message: Message, s: String) {
        if (message.mentionedUsers.isEmpty()) {
            message.channel.sendMessage(Emote.X + " - Mention a user to (un)blacklist.").queue()
        }
        val id = message.mentionedUsers[0].id
        if (data.blacklist.contains(id)) {
            data.blacklist.remove(id)
        } else {
            data.blacklist.add(id)
        }
        message.channel.sendMessage(Emote.SMILE + " - " + message.mentionedUsers[0].name + " has been " + (if (data.blacklist.contains(id)) "added to" else "removed from") + " the blacklist.").queue()
    }
}