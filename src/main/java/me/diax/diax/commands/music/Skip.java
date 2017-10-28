package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "skip",
        triggers = "skip"
)
public class Skip implements Command {

    @Override
    public void execute(Message message, String s) {
        if (!GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().isPlaying(message.getTextChannel())) {
            message.getChannel().sendMessage(Emote.X + " - There is nothing playing!").queue();
            return;
        }
        message.getTextChannel().sendMessage(Emote.MUSICAL_NOTE + " - Skipping the current track.").queue();
        GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().skip(message.getTextChannel());
    }
}