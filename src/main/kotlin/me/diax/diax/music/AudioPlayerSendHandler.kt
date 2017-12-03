package me.diax.diax.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.core.audio.AudioSendHandler

class AudioPlayerSendHandler(private val player: AudioPlayer) : AudioSendHandler {
    private var frame: AudioFrame? = null

    override fun canProvide(): Boolean {
        if (frame == null) frame = player.provide()
        return frame != null
    }

    override fun provide20MsAudio(): ByteArray? {
        if (frame == null) frame = player.provide()
        val data = if (frame != null) frame!!.data else null
        frame = null
        return data
    }

    override fun isOpus(): Boolean {
        return true
    }
}