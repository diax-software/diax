package com.github.diaxsoftware.diax.commands.rng;

import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.util.Util;
import com.github.rainestormee.jdacommand.CommandDescription;
import net.dv8tion.jda.api.entities.Message;

import java.util.Random;

@CommandDescription(name = "Flip", triggers = {"coin", "flip", "toss"})
public class FlipCommand implements DiaxCommand {

    @Override
    public void execute(Message message, String args) {
        Util.sendMessage(message.getChannel(), "The coin landed on: " + new String[]{"Heads!", "Tails!"}[new Random().nextInt(1)]);
    }
}
