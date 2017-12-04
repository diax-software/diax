package me.diax.diax.commands.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.music.MusicTrack
import me.diax.diax.util.Emote
import me.diax.diax.util.Emote.MUSICAL_NOTE
import me.diax.diax.util.Emote.X
import me.diax.diax.util.StringUtil
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
    name = "play",
    triggers = ["play"],
    description = "{url/query}"
)
class Play : Command {

    override fun execute(message: Message, args: String) {
        val manager = GuildMusicManager.getManagerFor(message.guild)
        if (args.isEmpty()) {
            message.textChannel.sendMessage(Emote.X + " - Pausing and stopping is currently not supported, please use `<>play [track url/YouTube query]` to queue a song.").queue()
            return
        }
        query(manager, message, args.trim(' '))
    }

    private fun query(manager: GuildMusicManager, message: Message, query: String) {
        val channel = message.textChannel
        manager.playerManager!!.loadItem(query, object : AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack) {
                message.textChannel.sendMessage("$MUSICAL_NOTE - Queuing `${StringUtil.stripMarkdown(track.info.title)} ` by `${StringUtil.stripMarkdown(track.info.author)} `.").queue()
                manager.scheduler.queue(MusicTrack(track, message.member, message.textChannel), channel)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val scheduler = manager.scheduler
                when (playlist.isSearchResult) {
                    true -> this.trackLoaded(playlist.tracks.first())
                    false -> {
                        if (playlist.selectedTrack != null) {
                            this.trackLoaded(playlist.selectedTrack)
                            return
                        }
                        message.textChannel.sendMessage("$MUSICAL_NOTE - Adding `${playlist.tracks.size}` tracks to the queue from the playlist `${StringUtil.stripMarkdown(playlist.name)} `.").queue()
                        playlist.tracks.forEach { track -> scheduler.queue(MusicTrack(track, message.member, message.textChannel), channel) }
                    }

                }
            }

            override fun noMatches() {
                if (!query.startsWith("Search Results:")) {
                    query(manager, message, "ytsearch: $query")
                } else {
                    message.textChannel.sendMessage("$X - No results found.").queue()
                }
            }

            override fun loadFailed(exception: FriendlyException) {
                message.textChannel.sendMessage("$X - Failed to load `$query` because `${exception.message}").queue()
            }
        })
    }
}