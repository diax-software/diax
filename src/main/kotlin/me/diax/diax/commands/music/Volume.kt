package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.MUSICAL_NOTE
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "volume", triggers = arrayOf("volume", "vol"),
        attributes = arrayOf(CommandAttribute(key = "patreon"), CommandAttribute(key = "category", value = "music")), description = "{0-150}")
class Volume : Command {

    override fun execute(trigger: Message, truncated: String) {
        val player = GuildMusicManager.getManagerFor(trigger.guild).player
        if (truncated.isEmpty()) {
            trigger.channel.sendMessage("$MUSICAL_NOTE - The volume is [${player.volume}/150]").queue()
            return
        }
        try {
            val volume = Integer.valueOf(truncated.split("\\s+".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0])!!
            if (volume > 150 || volume < 0) throw NumberFormatException("Invalid number in expected range.")
            player.volume = volume
            trigger.channel.sendMessage("$MUSICAL_NOTE - The volume has been set to [${player.volume}/150]").queue()
        } catch (e: NumberFormatException) {
            trigger.channel.sendMessage("$X - That is not a valid number [0/150]!").queue()
        }
    }
}