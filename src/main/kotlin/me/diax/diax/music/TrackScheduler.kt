package me.diax.diax.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import me.diax.diax.util.Emote
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.exceptions.PermissionException
import java.util.*
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque

class TrackScheduler(// The manager associated with this Scheduler
    private val manager: GuildMusicManager, // Last channel used.
    private var channel: TextChannel?) : AudioEventAdapter() {

    // The queue
    val queue: BlockingDeque<MusicTrack>
    // The previous track that was played, null if no previous track.
    private var current: MusicTrack? = null

    // The current track that is playing, null if no track currently being played.
    private var previous: MusicTrack? = null

    // True if the current track is on repeat.
    private var repeat: Boolean = false


    // Is music playing?
    private var playing: Boolean = false

    val isPlaying: Boolean
        get() = this.isPlaying(null)

    // Gets the current playing track.
    val currentTrack: MusicTrack?
        get() = if (current == null) null else current!!.makeClone()

    // Gets the previous track.
    val previousTrack: MusicTrack?
        get() = if (previous == null) null else previous!!.makeClone()

    init {
        this.current = null
        this.repeat = false
        this.playing = false
        this.previous = null
        this.queue = LinkedBlockingDeque()
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, reason: AudioTrackEndReason?) {
        if (reason!!.mayStartNext) { // If the reason will allow another track to start..
            if (repeat) { // If the current track is repeating, then play it again.
                player!!.playTrack(this.currentTrack!!.track)
            } else { // Else, play the next track.
                this.skip()
            }
        }
    }

    override fun onTrackStart(player: AudioPlayer?, track: AudioTrack?) {
        val guild = current!!.channel.guild
        if (guild == null) { // If the server doesn't exist, stop the track.
            this.stop(channel)
            return
        } else if (current!!.requester == null) { // If the person who requested the song doesn't exist, then skip.
            channel!!.sendMessage(Emote.X + " - The person who requested this track is no longer in the server, skipping...").queue()
            this.skip()
        }
        if (!joinVoiceChannel()) { // If couldn't join the voice channel, then stop.
            channel!!.sendMessage(Emote.X + " - Could not join the voice channel, stopping.").queue()
            stop(channel)
            return
        }
        this.sendEmbed(current!!) // Send music info to the channel.
    }

    // Can join voice channel? False: No, True: Yes
    private fun joinVoiceChannel(): Boolean {
        val guild = manager.guild
        val member = current!!.requester
        val voiceChannel = getVoiceChannel(member)
        if (!guild.audioManager.isConnected) {
            try {
                guild.audioManager.openAudioConnection(voiceChannel)
            } catch (exception: PermissionException) {
                return false
            }

        }
        return true
    }

    // Get the voice channel of a member.
    private fun getVoiceChannel(member: Member?): VoiceChannel? {
        if (member == null) return null
        val guild = member.guild
        var vc: VoiceChannel? = null
        if (member.voiceState.inVoiceChannel()) {
            vc = member.voiceState.channel
        } else if (!guild.voiceChannels.isEmpty()) {
            vc = guild.voiceChannels[0]
        }
        return vc
    }

    // If the current track is not null, then something is playing.
    fun isPlaying(channel: TextChannel?): Boolean {
        if (channel != null) this.channel = channel
        return playing
    }

    // Adds a MusicTrack to the queue.
    @Synchronized
    fun queue(track: MusicTrack) {
        this.queue(track, null)
    }

    @Synchronized
    fun queue(track: MusicTrack, channel: TextChannel?) {
        if (channel != null) this.channel = channel
        queue.offer(track)
        if (!this.isPlaying) this.skip() // If nothing is currently playing, then skip.
    }

    // Force skips the current track.
    @Synchronized
    fun skip() {
        this.skip(null)
    }

    @Synchronized
    fun skip(channel: TextChannel?) {
        if (channel != null) this.channel = channel
        if (current != null) previous = current
        repeat = false
        if (queue.isEmpty()) { // If there is nothing in the queue, then stop.
            this.stop(channel)
        } else { // Else, play the next track in the queue.
            val track = queue.poll()
            current = track
            this.play(track)

        }
    }

    @Synchronized
    fun stop(channel: TextChannel?) {
        manager.player.isPaused = false
        manager.player.volume = 100
        if (this.isPlaying || !this.queue.isEmpty()) {
            manager.player.stopTrack()
            this.channel!!.sendMessage(Emote.MUSICAL_NOTE + " - Queue concluded.").queue()
            previous = current
            current = null
            playing = false
            queue.clear()
        }
        Thread { manager.guild.audioManager.closeAudioConnection() }.run()
    }

    // Shuffle the queue.
    @Synchronized
    fun shuffle(): Boolean {
        return this.shuffle(null)
    }

    @Synchronized
    fun shuffle(channel: TextChannel?): Boolean {
        if (channel != null) this.channel = channel
        if (queue.isEmpty()) {
            return false
        }
        val tracks = ArrayList<MusicTrack>()
        queue.drainTo(tracks)
        Collections.shuffle(tracks)
        queue.addAll(tracks)
        return true
    }

    // Plays the given track.
    fun play(track: MusicTrack) {
        this.play(track, null)
    }

    fun play(track: MusicTrack, channel: TextChannel?) {
        if (channel != null) this.channel = channel
        if (current != null) previous = current
        playing = track.track != null
        manager.player.playTrack(track.track)
    }

    // Sets the track to repeating
    fun setRepeating(repeat: Boolean): Boolean {
        return this.setRepeating(repeat, null)
    }

    fun setRepeating(repeat: Boolean, channel: TextChannel?): Boolean {
        if (channel != null) this.channel = channel
        this.repeat = repeat
        return repeat
    }

    // Returns true if the track is repeating.
    fun isRepeating(): Boolean {
        return repeat
    }

    // Sends the now playing message to the selected channel.
    private fun sendEmbed(track: MusicTrack) {
        channel!!.sendMessage(Embed.music(track)).queue()
    }

    override fun onTrackException(player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        if (repeat) repeat = false
        channel!!.sendMessage(Emote.X + " - Failed to play the track due to: ```" + exception!!.message + " ```").queue()
        WebHookUtil.log(current!!.channel.jda, Emote.X + " An exception occurred.", "Failed to play a track due to: : ```" + (exception.message + " | " + current!!.channel.guild + " | " + StringUtil.stripMarkdown(current!!.channel)) + "```")
        skip()
    }

    override fun onTrackStuck(player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        channel!!.sendMessage(Emote.X + " - Got stuck attempting to play track, skipping.").queue()
        skip()
    }
}
