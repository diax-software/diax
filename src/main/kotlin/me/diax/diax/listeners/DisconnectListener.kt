package me.diax.diax.listeners

import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class DisconnectListener : ListenerAdapter() {

    override fun onGuildLeave(event: GuildLeaveEvent?) {
        close(event!!.guild)
    }

    override fun onGuildUnavailable(event: GuildUnavailableEvent?) {
        close(event!!.guild)
    }

    override fun onGuildVoiceMove(event: GuildVoiceMoveEvent?) {
        if (event!!.channelLeft.members.contains(event.guild.selfMember) && event.channelLeft.members.size < 2 || event.channelJoined.members.contains(event.guild.selfMember) && event.channelJoined.members.size < 2) {
            return
        }
        close(event.guild)
    }

    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent?) {
        if (event!!.channelLeft.members.contains(event.guild.selfMember) && event.channelLeft.members.size < 2) {
            close(event.guild)
        }
    }

    private fun close(guild: Guild) {
        Thread { guild.audioManager.closeAudioConnection() }.run()
    }
}