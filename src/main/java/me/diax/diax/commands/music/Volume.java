package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "volume",
        triggers = "volume",
        attributes = @CommandAttribute(key = "patreon"),
        description = "[0-100] | Sets the volume of Diax."
)
public class Volume implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
    }
}