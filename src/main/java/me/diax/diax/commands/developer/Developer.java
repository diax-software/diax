package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "developer",
        triggers = "developer",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")}
)
public class Developer implements Command {

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(message.getAuthor().getName() + " is an official Diax developer.").queue();
    }
}
