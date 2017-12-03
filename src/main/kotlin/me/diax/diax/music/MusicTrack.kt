package me.diax.diax.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel

class MusicTrack(val track: AudioTrack, val requester: Member, val channel: TextChannel) {

    fun makeClone(): MusicTrack {
        return MusicTrack(track.makeClone(), requester, channel)
    }
}