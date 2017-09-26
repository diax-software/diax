package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "report",
        description = "Report bugs easily!",
        triggers = "report",
        attributes = @CommandAttribute(key = "private")
)
public class Report implements Command {

    @Override
    public void execute(Message message, String s) {
        String error = "";
        if (s.length() < 149) {
            error = "Please provide more information.";
        } else if (s.length() > 500) {
            error = "Please try and make your report to the point.";
        }
        if (!error.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + "- " + error).queue();
            return;
        }
    }
}
