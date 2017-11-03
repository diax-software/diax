package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.MUSICAL_NOTE
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "stop", triggers = arrayOf("stop"))
class Stop : Command {

    override fun execute(message: Message, s: String) {
        GuildMusicManager.getManagerFor(message.guild).scheduler.stop(message.textChannel)
        message.textChannel.sendMessage("$MUSICAL_NOTE - Playback has been stopped.").queue()
    }
}