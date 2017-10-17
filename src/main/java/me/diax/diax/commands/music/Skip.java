package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "skip",
        triggers = "skip",
        description = "Skips the current track."
)
public class Skip implements Command {

    @Override
    public void execute(Message message, String s) {
        if (!GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().isPlaying()) {
            message.getChannel().sendMessage(Emote.X + " - There is nothing playing!").queue();
            return;
        }
        message.getTextChannel().sendMessage(Emote.X + " - Skipping...").queue();
        GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().skip(message.getTextChannel());
    }
}