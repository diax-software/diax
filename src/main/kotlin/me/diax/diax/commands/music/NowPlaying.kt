package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.X
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
        name = "nowplaying",
        triggers = ["song", "current", "nowplaying", "np"]
)
class NowPlaying : Command {

    override fun execute(message: Message, s: String) {
        val scheduler = GuildMusicManager.getManagerFor(message.guild).scheduler
        message.channel.sendMessage(if (!scheduler.isPlaying) "$X - There is no track playing!" else Embed.music(scheduler.currentTrack!!)).queue()
    }
}