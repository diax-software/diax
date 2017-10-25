package me.diax.diax.commands.fun;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

@CommandDescription(
        name = "8ball",
        triggers = {
                "8ball", "eightball", "8-ball"
        },
        description = "Ask a question to the 8ball.",
        attributes = {
                @CommandAttribute(key = "private"),
        }
)
public class EightBall implements Command {

    @Override
    public void execute(Message message, String s) {
        if (s.isEmpty() || !s.endsWith("?")) {
            message.getChannel().sendMessage(Emote.X + " - Specify a question to ask the 8-ball.").queue();
        } else {
            message.getChannel().sendMessage(Emote.EIGHT_BALL + " - " + new String[]{
                    "It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it",
                    "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes",
                    "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again",
                    "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"
            }[new Random().nextInt(20)]).queue();
        }
    }
}