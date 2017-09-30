package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "csgo",
        triggers = "csgo",
        attributes = @CommandAttribute(key = "private")
)
public class CSGO implements Command {

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage("*Hello am 48 year man from somalia. Sorry for my bed england. I selled my wife for internet connection for play \"conter stirk and i want to become the goodest player like you I play with 400 ping on brazil server and i am Global elite 2. pls no copy pasterino my story.*").queue();
    }
}
