package me.diax.diax.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import java.util.*

class GuildMusicManager
@JvmOverloads constructor(val guild: Guild, channel: TextChannel = guild.textChannels[0]) {

    val player: AudioPlayer
    val scheduler: TrackScheduler

    val playerManager: AudioPlayerManager?
        get() = MANAGER

    val sendHandler: AudioPlayerSendHandler
        get() = AudioPlayerSendHandler(player)

    init {
        player = MANAGER.createPlayer()
        scheduler = TrackScheduler(this, channel)
        player.addListener(scheduler)
        guild.audioManager.sendingHandler = this.sendHandler
    }

    companion object {

        private var MANAGERS: MutableMap<String, GuildMusicManager> = HashMap()
        private var MANAGER: DefaultAudioPlayerManager = DefaultAudioPlayerManager()

        init {
            AudioSourceManagers.registerRemoteSources(MANAGER)
            AudioSourceManagers.registerLocalSource(MANAGER)
        }

        fun getManagerFor(guild: Guild): GuildMusicManager {
            return MANAGERS.computeIfAbsent(guild.id) { GuildMusicManager(guild) }
        }

        fun removeManagerFor(guild: Guild) {
            MANAGERS.remove(guild.id)
        }
    }
}