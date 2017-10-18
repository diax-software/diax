package me.diax.diax.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    // The queue
    private final BlockingDeque<MusicTrack> queue;
    // The manager associated with this Scheduler
    private GuildMusicManager manager;
    // The previous track that was played, null if no previous track.
    private MusicTrack current;

    // The current track that is playing, null if no track currently being played.
    private MusicTrack previous;

    // True if the current track is on repeat.
    private boolean repeat;

    // Last channel used.
    private TextChannel channel;

    public TrackScheduler(GuildMusicManager manager, TextChannel channel) {
        this.current = null;
        this.repeat = false;
        this.previous = null;
        this.manager = manager;
        this.channel = channel;
        this.queue = new LinkedBlockingDeque<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext) { // If the reason will allow another track to start..
            if (repeat) { // If the current track is repeating, then play it again.
                player.playTrack(this.getCurrentTrack().getTrack());
            } else { // Else, play the next track.
                this.skip();
            }
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Guild guild = current.getChannel().getGuild();
        if (guild == null) { // If the server doesn't exist, stop the track.
            this.stop();
            return;
        } else if (current.getRequester() == null) { // If the person who requested the song doesn't exist, then skip.
            channel.sendMessage(Emote.X + " - The person who requested this track is no longer in the server, skipping...").queue();
            this.skip();
        }
        if (!joinVoiceChannel()) { // If couldn't join the voice channel, then stop.
            channel.sendMessage(Emote.X + " - Could not join the voice channel, stopping.").queue();
            stop();
            return;
        }
        this.sendEmbed(current); // Send music info to the channel.
    }

    // Can join voice channel? False: No, True: Yes
    private boolean joinVoiceChannel() {
        Guild guild = manager.getGuild();
        Member member = current.getRequester();
        VoiceChannel voiceChannel = getVoiceChannel(member);
        if (!guild.getAudioManager().isConnected()) {
            try {
                guild.getAudioManager().openAudioConnection(voiceChannel);
            } catch (PermissionException exception) {
                return false;
            }
        }
        return true;
    }

    // Get the voice channel of a member.
    private VoiceChannel getVoiceChannel(Member member) {
        if (member == null) return null;
        Guild guild = member.getGuild();
        VoiceChannel vc = null;
        if (member.getVoiceState().inVoiceChannel()) {
            vc = member.getVoiceState().getChannel();
        } else if (!guild.getVoiceChannels().isEmpty()) {
            vc = guild.getVoiceChannels().get(0);
        }
        return vc;
    }

    public boolean isPlaying() {
        return this.isPlaying(null);
    }

    // If the current track is not null, then something is playing.
    public boolean isPlaying(TextChannel channel) {
        if (channel != null) this.channel = channel;
        return current != null;
    }

    // Adds a MusicTrack to the queue.
    public synchronized void queue(MusicTrack track) {
        this.queue(track, null);
    }

    public synchronized void queue(MusicTrack track, TextChannel channel) {
        if (channel != null) this.channel = channel;
        queue.offer(track);
        if (!this.isPlaying()) this.skip(); // If nothing is currently playing, then skip.
    }

    // Force skips the current track.
    public synchronized void skip() {
        this.skip(null);
    }

    public synchronized void skip(TextChannel channel) {
        if (channel != null) this.channel = channel;
        if (current != null) previous = current;
        repeat = false;
        if (queue.isEmpty()) { // If there is nothing in the queue, then stop.
            this.stop();
        } else { // Else, play the next track in the queue.
            MusicTrack track = queue.poll();
            current = track;
            this.play(track);

        }
    }

    // Stops and cleans up the player.
    public synchronized void stop() {
        this.stop(null);
    }

    public synchronized void stop(TextChannel channel) {
        if (channel != null) this.channel = channel;
        manager.getPlayer().setPaused(false);
        manager.getPlayer().setVolume(100);
        if (isPlaying()) {
            manager.getPlayer().stopTrack();
            this.channel.sendMessage(Emote.MUSICAL_NOTE + " - Queue concluded.").queue();
            previous = current;
            current = null;
        }
        manager.getGuild().getAudioManager().closeAudioConnection();
    }

    // Shuffle the queue.
    public synchronized boolean shuffle() {
        return this.shuffle(null);
    }

    public synchronized boolean shuffle(TextChannel channel) {
        if (channel != null) this.channel = channel;
        if (queue.isEmpty()) {
            return false;
        }
        List<MusicTrack> tracks = new ArrayList<>();
        queue.drainTo(tracks);
        Collections.shuffle(tracks);
        queue.addAll(tracks);
        return true;
    }

    // Plays the given track.
    public void play(MusicTrack track) {
        this.play(track, null);
    }

    public void play(MusicTrack track, TextChannel channel) {
        if (channel != null) this.channel = channel;
        if (current != null) previous = current;
        manager.getPlayer().playTrack(track.getTrack());
    }

    // Gets the current playing track.
    public MusicTrack getCurrentTrack() {
        if (current == null) return null;
        return current.makeClone();
    }

    // Gets the previous track.
    public MusicTrack getPreviousTrack() {
        if (previous == null) return null;
        return previous.makeClone();
    }

    // Gets the queue.
    public synchronized List<MusicTrack> getQueue() {
        List<MusicTrack> tracks = new ArrayList<>();
        tracks.addAll(queue);
        return tracks;
    }

    // Sends the now playing message to the selected channel.
    private void sendEmbed(MusicTrack track) {
        channel.sendMessage(Embed.music(track)).queue();
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        if (repeat) repeat = false;
        channel.sendMessage(Emote.X + " - Failed to play the track due to: ```" + exception.getMessage() + " ```").queue();
        WebHookUtil.log(current.getChannel().getJDA(), Emote.X + " An exception occurred.", "Failed to play a track due to: : ```" + (exception.getMessage() + " | " + current.getChannel().getGuild() + " | " + StringUtil.stripMarkdown(current.getChannel())) + "```");
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        channel.sendMessage(Emote.X + " - Got stuck attempting to play track, skipping.").queue();
        skip();
    }
}
