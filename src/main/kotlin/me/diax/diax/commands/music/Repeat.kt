package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.MUSICAL_NOTE
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "repeat", triggers = arrayOf("repeat"),
        attributes = arrayOf(CommandAttribute(key = "category", value = "music")))
class Repeat : Command {

    override fun execute(trigger: Message, truncated: String) {
        val scheduler = GuildMusicManager.getManagerFor(trigger.guild).scheduler
        trigger.channel.sendMessage("$MUSICAL_NOTE - The current track is ${if (scheduler.setRepeating(!scheduler.isPlaying, trigger.textChannel)) "now" else "no longer"} repeating.").queue()
    }
}