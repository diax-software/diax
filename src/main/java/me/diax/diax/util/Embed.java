package me.diax.diax.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.diax.diax.music.MusicTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;

public class Embed {

    public static EmbedBuilder themed() {
        return new EmbedBuilder().setColor(BotType.CURRENT_TYPE.getMainColor());
    }

    public static EmbedBuilder transparent() {
        return new EmbedBuilder().setColor(new Color(54, 57, 62));
    }

    public static String music(MusicTrack track) {
        AudioTrackInfo info = track.getTrack().getInfo();
        Member requester = track.getRequester();
        return String.format(Emote.MUSICAL_NOTE + " - Now playing: `%s ` by `%s `" + (requester == null ? "" : "\nRequested by: `" + requester.getEffectiveName() + " `"), info.title, info.author);
    }
}