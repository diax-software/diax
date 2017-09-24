package me.diax.diax.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public class MusicTrack {

    private AudioTrack track;
    private Member requester;
    private TextChannel channel;

    public MusicTrack(AudioTrack track, Member requester, TextChannel channel) {
        this.track = track;
        this.requester = requester;
        this.channel = channel;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public Member getRequester() {
        return requester;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public MusicTrack makeClone() {
        return new MusicTrack(track.makeClone(), requester, channel);
    }
}