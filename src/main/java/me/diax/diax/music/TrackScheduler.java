package me.diax.diax.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final BlockingQueue<MusicTrack> queue;
    private GuildMusicManager manager;
    private boolean repeating = false;

    private MusicTrack current;
    private MusicTrack last;

    public TrackScheduler(GuildMusicManager manager) {
        this.manager = manager;
        this.queue = new LinkedBlockingQueue<>();
    }

    public BlockingQueue<MusicTrack> getQueue() {
        return queue;
    }

    public boolean play(MusicTrack track) {
        if (track != null) {
            current = track;
            return manager.getPlayer().startTrack(track.getTrack(), false);
        }
        return false;
    }

    public void skip() {
        last = current;
        if (repeating) {
            if (current != null) {
                play(current.makeClone());
            } else if (!queue.isEmpty()) {
                play(queue.poll());
            }
        } else if (queue.isEmpty()) {
            if (current != null) {
                current.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - Queue concluded!").queue();
            }
            stop();
        } else {
            play(queue.poll());
        }
    }

    public void stop() {
        queue.clear();
        manager.getPlayer().stopTrack();
        current = null;
        last = null;
        manager.getGuild().getAudioManager().closeAudioConnection();
    }

    public void queue(MusicTrack track) {
        if (queue.offer(track) && current == null) {
            skip();
        }
    }

    public GuildMusicManager getManager() {
        return manager;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public MusicTrack getCurrent() {
        return current;
    }

    public MusicTrack getLast() {
        return last;
    }

    public VoiceChannel getVoiceChannel(Guild guild, Member member) {
        VoiceChannel vc = null;
        if (member != null && member.getVoiceState().inVoiceChannel()) {
            vc = member.getVoiceState().getChannel();
        } else if (!guild.getVoiceChannels().isEmpty()) {
            vc = guild.getVoiceChannels().get(0);
        }
        return vc;
    }

    public boolean joinVoiceChannel() {
        Guild guild = manager.getGuild();
        Member member = current.getRequester();
        VoiceChannel voiceChannel = getVoiceChannel(guild, member);
        if (!guild.getAudioManager().isConnected() || this.queue.isEmpty()) {
            try {
                guild.getAudioManager().openAudioConnection(voiceChannel);
            } catch (PermissionException exception) {
                return false;
            }
        }
        return true;
    }

    public boolean shuffle() {
        try {
            if (!queue.isEmpty()) {
                List<MusicTrack> tracks = new ArrayList<>();
                queue.drainTo(tracks);
                Collections.shuffle(tracks);
                queue.addAll(tracks);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if (joinVoiceChannel()) {
            if (!repeating) {
                AudioTrackInfo info = current.getTrack().getInfo();
                Member requester = current.getRequester();
                current.getChannel().sendMessage(String.format(Emote.MUSICAL_NOTE + " - Now playing: `%s ` by `%s `\nRequested by: `%s `", info.title, info.author, requester.getEffectiveName())).queue();
            } else {
                current.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - Repeating the last song.").queue();
            }
        } else {
            current.getChannel().sendMessage(Emote.X + " - Could not join that voice channel!").queue();
            stop();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason) {
        if (reason.mayStartNext) {
            skip();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        current.getChannel().sendMessage(Emote.X + " - Failed to play the track due to: ```" + exception.getMessage() + " ```").queue();
        WebHookUtil.log(current.getChannel().getJDA(), Emote.X + " An exception occurred.", "Failed to play a track due to: : ```" + (exception.getMessage() + " | " + current.getChannel().getGuild() + " | " + current.getChannel()).replace("`", "\\`") + "```");
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        current.getChannel().sendMessage(Emote.X + " - Got stuck attempting to play track, skipping.").queue();
        skip();
    }
}