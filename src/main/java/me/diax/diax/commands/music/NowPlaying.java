package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.music.TrackScheduler;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
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
        TrackScheduler scheduler = GuildMusicManager.getManagerFor(message.getGuild()).getScheduler();
        message.getChannel().sendMessage(!scheduler.isPlaying() ? Emote.X + " - There is no track playing!" : Embed.music(scheduler.getCurrentTrack())).queue();
    }
}