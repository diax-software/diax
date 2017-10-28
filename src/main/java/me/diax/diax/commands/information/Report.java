package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "report",
        description = "[description]",
        triggers = "report",
        attributes = {
                @CommandAttribute(key = "private")
        }
)
public class Report implements Command {

    @Override
    public void execute(Message message, String s) {
        String error = "";
        if (s.length() < 20) {
            error = "Please provide more information.";
        } else if (s.length() > 500) {
            error = "Please try and keep your report to the point.";
        }
        if (!error.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + "- " + error).queue();
            return;
        }
        WebHookUtil.report(message.getJDA(), "```" + StringUtil.stripMarkdown(s) + "```\nReporter: *" + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + "*");
        message.getChannel().sendMessage(Emote.SMILE + " - Reported successfully, join here if you need more assistance: https://discord.gg/5sJZa2y").queue();
    }
}
