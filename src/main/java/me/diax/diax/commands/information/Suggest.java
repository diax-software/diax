package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.JDA;

import java.util.Random;

@CommandDescription(
    name = "suggest",
    triggers = "suggest"
)
public class Suggest implements Command {

    private JDA jda;

    public Suggest(JDA jda) {
      this.jda = jda;
    }

    @Override
    public void execute(Message message, String args) {
        if (args.isEmpty()) {
            message.getTextChannel().sendMessage("Please give something to suggest").queue();
            return;
        }
        sendSuggestion(args);
    }
    
    private void sendSuggestion(String s) {
      jda.getTextChannelById("356564127864586240").sendMessage("New Suggestion: " + s).queue(); //sends message to #suggestions in diax server
    }
}
