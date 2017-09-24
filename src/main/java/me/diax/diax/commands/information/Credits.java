package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "credits",
        triggers = "credits"
)
public class Credits implements Command {

    @Override
    public void execute(Message message, String s) {
        message.getTextChannel().sendMessage(
                "There are now too many people to just single out, I have come to understand that Diax is driven and continue to be driven by the community, by you. Thank you for supporting me. Thank you for making Diax possible."
        ).queue();
    }
}