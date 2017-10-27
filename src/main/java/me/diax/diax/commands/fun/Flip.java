package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

@CommandDescription(
        name = "flip",
        triggers = {
                "flip", "coin", "heads", "toss", "tails", "hot"
        },
        attributes = {
                @CommandAttribute(key = "private"),
        }
)
public class Flip implements Command {

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Emote.BUTTON + " - You flipped a " + (new Random().nextInt(2) == 1 ? "heads" : "tails") + ".").queue();
    }
}