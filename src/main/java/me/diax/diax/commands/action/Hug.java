package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.stream.Collectors;

/**
 * This is the first action command to be made.
 * Test cases:
 * <>hug @mention - message author is hugging @mention
 * <>hug @author - Diax is hugging @author
 * <>hug @mention1, @mention2 - @author is hugging @mention1 and @mention2
 * <>hug @mention1, @mention2, @mention3 - @author is hugging @mention1, @mention2 and @mention3
 * <>hug @mention1, @author - Diax is hugging @author and @mention1
 * <>hug - Please mention somebody to hug.
 *
 * @author comportment
 * @since 0.0.1
 */
@CommandDescription(
        name = "hug",
        triggers = "hug",
        description = "[@mention...]"
)
public class Hug implements ActionCommand {

    @Override
    public String[] getImages() {
        return new String[]{
                "qscdhWs5o3yb6",
                "BXrwTdoho6hkQ",
                "BXrwTdoho6hkQ",
                "143v0Z4767T15e",
                "a5s3dI6Wv1Qcw",
                "trJ68zLtt85QA",
                "ArLxZ4PebH2Ug",
                "wSY4wcrHnB0CA",
                "wnsgren9NtITS",
                "pXQhWw2oHoPIs",
                "8tpiC1JAYVMFq",
                "lXiRKBj0SAA0EWvbG"
        };
    }

    @Override
    public void execute(Message message, String s) {
        String msg = "***";
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Please @mention somebody to hug.").queue();
            return;
        }
        if (message.getMentionedUsers().contains(message.getAuthor())) {
            msg += "Diax ";
        } else {
            msg += message.getMember().getEffectiveName();
        }
        msg += " is hugging " + StringUtil.stripMarkdown(message.getMentionedUsers().stream().map(User::getName).collect(Collectors.joining(", "))) + "***";
        message.getChannel().sendMessage(Embed.transparent().setDescription(msg).setImage(this.getImage()).build()).queue();
    }
}