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
        message.getTextChannel().sendMessage(Emote.X + " - Skipping...").queue();
        GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().skip();
    }
}