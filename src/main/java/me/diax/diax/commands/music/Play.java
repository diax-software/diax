package me.diax.diax.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.music.TrackScheduler;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "play",
        triggers = "play",
        description = "[url/query] | Plays a track from a URL or query."
)
public class Play implements Command {

    @Override
    public void execute(Message message, String args) {
        GuildMusicManager manager = GuildMusicManager.getManagerFor(message.getGuild());
        if (args.isEmpty()) {
            message.getTextChannel().sendMessage(Emote.X + " - Pausing and stopping is currently not supported, please use `<>play [track url/YouTube query]` to queue a song.").queue();
            return;
        }
        query(manager, message, args.trim());
    }

    private void query(GuildMusicManager manager, Message message, String query) {
        manager.getPlayerManager().loadItem(query, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                message.getTextChannel().sendMessage(Emote.MUSICAL_NOTE + String.format(" - Queuing `%s ` by `%s. `", StringUtil.stripMarkdown(track.getInfo().title), StringUtil.stripMarkdown(track.getInfo().author))).queue();
                manager.getScheduler().queue(new MusicTrack(track, message.getMember(), message.getTextChannel()));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                TrackScheduler scheduler = manager.getScheduler();
                if (playlist.isSearchResult()) {
                    if (playlist.getTracks().isEmpty()) return;
                    this.trackLoaded(playlist.getTracks().get(0));
                } else if (playlist.getSelectedTrack() != null) {
                    this.trackLoaded(playlist.getSelectedTrack());
                } else {
                    message.getTextChannel().sendMessage(Emote.MUSICAL_NOTE + String.format(" - Adding `%s ` tracks to the queue from the playlist `%s `.", playlist.getTracks().size(), StringUtil.stripMarkdown(playlist.getName()))).queue();
                    playlist.getTracks().forEach(track -> scheduler.queue(new MusicTrack(track, message.getMember(), message.getTextChannel())));
                }
            }

            @Override
            public void noMatches() {
                if (!query.startsWith("Search Results:")) {
                    query(manager, message, "ytsearch: " + query);
                } else {
                    message.getTextChannel().sendMessage(Emote.X + " - No results found.").queue();
                }
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                message.getTextChannel().sendMessage(Emote.X + String.format(" - Failed to load `%s` because `%s`.", query, exception.getMessage())).queue();
            }
        });
    }
}