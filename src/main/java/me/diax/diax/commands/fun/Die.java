package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

@CommandDescription(
        name = "die",
        triggers = {
                "die", "dice", "roll"
        },
        description = "{number} | Rolls a six-sided or the amount given die.",
        attributes = {
                @CommandAttribute(key = "private"),
        }
)
public class Die implements Command {

    private Random random;

    public Die() {
        random = new Random();
    }

    @Override
    public void execute(Message message, String s) {
        int result;
        if (s.isEmpty()) {
            result = random.nextInt(6);
        } else {
            try {
                result = random.nextInt(Integer.valueOf(s.split("\\s+")[0]));
            } catch (NumberFormatException e) {
                message.getChannel().sendMessage(Emote.X + " - That isn't a valid number!").queue();
                return;
            }
        }
        message.getChannel().sendMessage(Emote.GAME_DIE + " - You rolled a `" + (result + 1) + "`").queue();
    }
}