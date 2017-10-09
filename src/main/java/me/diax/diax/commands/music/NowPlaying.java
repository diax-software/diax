package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "nowplaying",
        triggers = {
                "song",
                "current",
                "nowplaying",
                "np"
        },
        description = "Shows the currently playing song."
)
public class NowPlaying implements Command {

    @Override
    public void execute(Message message, String s) {
        MusicTrack track = GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().getCurrentTrack();
        message.getChannel().sendMessage(track == null ? Emote.X + " - No song is currently playing in this guild." : Emote.MUSICAL_NOTE + " - Now playing: `" + StringUtil.stripMarkdown(track.getTrack().getInfo().title) + " ` by: `" + StringUtil.stripMarkdown(track.getTrack().getInfo().author) + " `\n\nRequested by: `" + "`").queue();
    }
}