package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "nowplaying",
        triggers = {
                "song",
                "current",
                "nowplaying",
                "np"
        }
)
public class NowPlaying implements Command {

    @Override
    public void execute(Message message, String s) {
        MusicTrack track = GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().getCurrent();
        message.getChannel().sendMessage(track == null ? Emote.X + " - No song is currently playing in this guild." : Emote.MUSICAL_NOTE + "- Now playing: `" + track.getTrack().getInfo().title.replace("`", "\\`") + " ` by: `" + track.getTrack().getInfo().author.replace("`", "\\`") + " `").queue();
    }
}