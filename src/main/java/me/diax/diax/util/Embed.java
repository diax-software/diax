package me.diax.diax.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.diax.diax.music.MusicTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.Color;

public class Embed {

    public static EmbedBuilder themed() {
        return new EmbedBuilder().setColor(BotType.CURRENT_TYPE.getMainColor());
    }

    public static EmbedBuilder transparent() {
        return new EmbedBuilder().setColor(Colors.DARK);
    }

    public static String music(MusicTrack track) {
        AudioTrackInfo info = track.getTrack().getInfo();
        Member requester = track.getRequester();
        return String.format(Emote.MUSICAL_NOTE + " - Now playing: `%s ` by `%s `" + (requester == null ? "" : "\nRequested by: `" + requester.getEffectiveName() + " `"), info.title, info.author);
    }

    public static MessageEmbed error(String content) {
        return new EmbedBuilder().setColor(Color.RED).setDescription(Emote.X + " - " + content).build();
    }
}