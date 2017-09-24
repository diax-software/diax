package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
    name = "shuffle",
    triggers = "shuffle"
)
public class Shuffle implements Command {

    @Override
    public void execute(Message message, String s) {
        if (GuildMusicManager.getManagerFor(message.getGuild()).getScheduler().shuffle()) {
            message.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - The queue has been shuffled!").queue();
        } else {
            message.getChannel().sendMessage(Emote.X + " - Could not shuffle the queue!").queue();
        }
    }
}
