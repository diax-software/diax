package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "repeat",
        triggers = "repeat"
)
public class Repeat implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
        trigger.getTextChannel().sendMessage(Emote.X + " - This is a patreon feature only.").queue();
    }
}