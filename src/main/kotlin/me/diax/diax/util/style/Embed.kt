package me.diax.diax.util.style

import me.diax.diax.music.MusicTrack
import me.diax.diax.util.Emote
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.MessageEmbed

object Embed {
    fun themed(): EmbedBuilder = EmbedBuilder().setColor(BotType.CURRENT_TYPE.mainColor)
    fun error(content: String): MessageEmbed = EmbedBuilder().setColor(Colors.RED).setDescription(Emote.X + " - " + content).build()
    fun transparent(): EmbedBuilder = EmbedBuilder().setColor(Colors.BLACKY)

    fun music(track: MusicTrack): String {
        val info = track.track.info
        val requester = track.requester
        return "${Emote.MUSICAL_NOTE} - Now playing: `${info.title}` by `${info.author} `\nRequested by: `${requester.effectiveName} `"
    }
}