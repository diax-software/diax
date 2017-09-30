package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "volume",
        triggers = "volume",
        attributes = @CommandAttribute(key = "patreon")
)
public class Volume implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
    }
}