package com.github.diaxsoftware.diax.commands.rng;

import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.util.Util;
import com.github.rainestormee.jdacommand.CommandDescription;
import net.dv8tion.jda.api.entities.Message;

import java.util.Random;

@CommandDescription(name = "Eightball", triggers = {"8ball", "eightball"})
public class EightBallCommand implements DiaxCommand {

    @Override
    public void execute(Message message, String args) {
        Util.sendMessage(message.getChannel(), "The magic 8-ball responds with: " + new String[]{"It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful"}[new Random().nextInt(20)]);
    }
}
