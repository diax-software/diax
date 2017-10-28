package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "say",
        triggers = {
                "say"
        },
        description = "[message]",
        attributes = {
                @CommandAttribute(key = "private"),
        }
)
public class Say implements Command {

    @Override
    public void execute(Message message, String s) {
        if (s.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Specify something to say.").queue();
        } else {
            message.getChannel().sendMessage(s).queue();
        }
    }
}