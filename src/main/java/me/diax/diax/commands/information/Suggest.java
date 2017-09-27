package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "suggest",
        description = "Suggest features easily!",
        triggers = "suggest",
        attributes = @CommandAttribute(key = "private")
)
public class Suggest implements Command {

    @Override
    public void execute(Message message, String s) {
        String error = "";
        if (s.length() < 149) {
            error = "Please provide more information.";
        } else if (s.length() > 500) {
            error = "Please try and keep your suggestion to the point.";
        }
        if (!error.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + "- " + error).queue();
            return;
        }
        WebHookUtil.suggest(message.getJDA(), "```" + StringUtil.stripMarkdown(s) + "```\nSuggester: *" + message.getAuthor().getName() + "#" + message.getAuthor().getDiscriminator() + "*");
    }
}

