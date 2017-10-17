package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "repeat",
        triggers = "repeat",
        description = "Toggles the repeating function."
)
public class Repeat implements Command {

    @Override
    public void execute(Message trigger, String truncated) {

    }
}