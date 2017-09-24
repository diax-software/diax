package me.diax.diax.commands.owner;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "announce",
        triggers = "announce",
        attributes = @CommandAttribute(key = "owner")
)
public class Announce implements Command {

    @Override
    public void execute(Message message, String s) {
        WebHookUtil.announce(message.getJDA(), s);
    }
}