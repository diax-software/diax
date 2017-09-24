package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;

import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "stop",
        triggers = "stop"
)
public class Stop implements Command {

    @Override
    public void execute(Message message, String s) {
        GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().stop();
        message.getTextChannel().sendMessage(Emote.MUSICAL_NOTE + " - Playback has been stopped.").queue();
    }
}