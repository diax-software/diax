package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.TrackScheduler;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "repeat",
        triggers = "repeat"
)
public class Repeat implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
        TrackScheduler scheduler = GuildMusicManager.getManagerFor(trigger.getGuild()).getScheduler();
        trigger.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - The current track is " + (scheduler.setRepeating(!scheduler.isPlaying(), trigger.getTextChannel()) ? "now" : "no longer") + "repeating.").queue();
    }
}