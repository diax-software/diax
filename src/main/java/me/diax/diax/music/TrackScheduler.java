package me.diax.diax.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Copied from Tohsaka source found: https://github.com/NachtRaben/TohsakaBot/blob/master/TohsakaCore/src/main/java/com/nachtraben/core/audio/TrackScheduler.java
 *
 * @author NachtRaben
 */
public class TrackScheduler extends AudioEventAdapter {

    private GuildMusicManager manager;

    private final BlockingDeque<MusicTrack> queue;

    private MusicTrack lastTrack;
    private MusicTrack currentTrack;

    private boolean repeatTrack;
    private boolean repeatQueue;

    private boolean channelLock;

    private ScheduledFuture<?> afkCheck;
    private long leave = -1;
    private boolean persist = true;


    public TrackScheduler(GuildMusicManager guildMusicManager) {
        this.manager = guildMusicManager;
        queue = new LinkedBlockingDeque<>();
        afkCheck = Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            Guild g = manager.getGuild();
            if (g != null && !persist) {
                VoiceChannel v = g.getAudioManager().getConnectedChannel();
                if (v != null) {
                    if ((v.getMembers().size() < 2 || currentTrack == null) && leave == -1) {
                        leave = TimeUnit.MINUTES.toMillis(2) + System.currentTimeMillis();
                    } else if (v.getMembers().size() > 1 && currentTrack != null && leave != -1) {
                        leave = -1;
                    } else if ((v.getMembers().size() < 2 || currentTrack == null) && leave != -1 && System.currentTimeMillis() > leave) {
                        stop();
                        Executors.newSingleThreadExecutor().execute(() -> {
                            g.getAudioManager().closeAudioConnection();
                        });
                    }
                } else if (leave != -1) {
                    leave = -1;
                }
            }
        }, 0L, 5L, TimeUnit.SECONDS);
    }

    public void queue(MusicTrack track) {
        synchronized (queue) {
            synchronized (queue) {
                queue.offer(track);
            }
            if (!isPlaying()) skip();
        }
    }

    public void play(MusicTrack track) {
        currentTrack = track;
        manager.getPlayer().playTrack(track.getTrack());
    }

    public void stop() {
        synchronized (queue) {
            repeatTrack = false;
            repeatQueue = false;
            manager.getPlayer().setPaused(false);
            queue.clear();
            if (isPlaying()) {
                manager.getPlayer().stopTrack();
                currentTrack.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - Queue concluded!").queue();
            }
            channelLock = false;
            currentTrack = null;
        }
    }

    public void skip() {
        synchronized (queue) {
            repeatTrack = false;
            lastTrack = currentTrack;
            if (currentTrack != null && repeatQueue)
                queue.addLast(getCurrentTrack());
            if (queue.isEmpty() && isPlaying()) {
                stop();
            } else if (!queue.isEmpty()) {
                play(queue.poll());
            }
        }
    }

    public boolean shuffle() {
        synchronized (queue) {
            if (!queue.isEmpty()) {
                List<MusicTrack> tracks = new ArrayList<>();
                queue.drainTo(tracks);
                Collections.shuffle(tracks);
                queue.addAll(tracks);
                return true;
            }
            return false;
        }
    }

    public boolean isPlaying() {
        return currentTrack != null && channelLock;
    }

    private boolean joinVoiceChannel() {

        Guild guild = manager.getGuild();
        Member member = currentTrack.getRequester();
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

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        Guild guild = currentTrack.getRequester().getGuild();
        if (guild == null) {
            stop();
            player.destroy();
            return;
        }
        if (currentTrack.getRequester() == null) {
            skip();
            return;
        }
        if (!joinVoiceChannel()) {
            currentTrack.getChannel().sendMessage(Emote.X + " - Could not join that voice channel!").queue();
            stop();
            return;
        }
        if (!repeatTrack) {
            sendEmbed(new MusicTrack(track, null, null));
        }
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

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext && repeatTrack) {
            player.playTrack(getCurrentTrack().getTrack());
        } else if (endReason.mayStartNext) {
            skip();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        if (repeatTrack)
            repeatTrack = false;
        currentTrack.getChannel().sendMessage(Emote.X + " - Failed to play the track due to: ```" + exception.getMessage() + " ```").queue();
        WebHookUtil.log(currentTrack.getChannel().getJDA(), Emote.X + " An exception occurred.", "Failed to play a track due to: : ```" + (exception.getMessage() + " | " + currentTrack.getChannel().getGuild() + " | " + StringUtil.stripMarkdown(currentTrack.getChannel())) + "```");
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        currentTrack.getChannel().sendMessage(Emote.X + " - Got stuck attempting to play track, skipping.").queue();
        skip();
    }

    private void sendEmbed(MusicTrack track) {
        AudioTrackInfo info = track.getTrack().getInfo();
        Member requester = track.getRequester();
        track.getChannel().sendMessage(String.format(Emote.MUSICAL_NOTE + " - Now playing: `%s ` by `%s `" + (requester == null ? "" : "\nRequested by: `" + requester.getEffectiveName() + " `"), info.title, info.author)).queue();
    }

    public List<MusicTrack> getQueue() {
        List<MusicTrack> tracks = new ArrayList<>();
        synchronized (queue) {
            tracks.addAll(queue);
        }
        return tracks;
    }

    public void skipTo(AudioTrack track) {
        synchronized (queue) {
            if (queue.contains(track)) {
                while (queue.peek() != track) {
                    if (repeatQueue)
                        queue.addLast(queue.pop());
                    else
                        queue.pop();
                }
                skip();
            }
        }
    }

    public MusicTrack getLastTrack() {
        if (lastTrack != null) {
            return lastTrack.makeClone();
        }
        return null;
    }

    public MusicTrack getCurrentTrack() {
        if (currentTrack != null) {
            return currentTrack.makeClone();
        }
        return null;
    }

    public boolean isRepeatTrack() {
        return repeatTrack;
    }

    public void setRepeatTrack(boolean repeatTrack) {
        this.repeatTrack = repeatTrack;
    }

    public boolean isRepeatQueue() {
        return repeatQueue;
    }

    public void setRepeatQueue(boolean repeatQueue) {
        this.repeatQueue = repeatQueue;
    }

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean b) {
        this.persist = b;
    }

    public void destroy() {
        if (afkCheck != null && !afkCheck.isCancelled())
            afkCheck.cancel(true);
    }
}