package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "announce",
        triggers = "announce",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Announce implements Command {

    @Override
    public void execute(Message message, String s) {
        WebHookUtil.announce(message.getJDA(), s + " - *" + message.getAuthor().getName() + "*");
    }
}