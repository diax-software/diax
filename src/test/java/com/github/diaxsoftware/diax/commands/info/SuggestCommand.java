package com.github.diaxsoftware.diax.commands.info;

import com.github.diaxsoftware.diax.commands.DiaxCommand;
import com.github.diaxsoftware.diax.util.Util;
import com.github.rainestormee.jdacommand.CommandDescription;
import net.dv8tion.jda.api.entities.Message;

@CommandDescription(name = "Suggest", triggers = {"suggest"})
public class SuggestCommand implements DiaxCommand {

    @Override
    public void execute(Message message, String args) {
        if (args.length() < 20) {
            Util.sendMessage(message.getChannel(), "Please provide more detail for your suggestion.");
            return;
        }
        Util.sendMessage(message.getJDA().getTextChannelById(584493875436060707L), "New Suggestion by " + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + "\n" + args);
        Util.sendMessage(message.getChannel(), "Suggestion received, thank you!");
    }
}
