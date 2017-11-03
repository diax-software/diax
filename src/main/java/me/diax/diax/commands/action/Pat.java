package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import javax.inject.Inject;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CommandDescription(
        name = "pat",
        triggers = "pat",
        attributes = {
                @CommandAttribute(key = "allowPrivate")
        }
)
public class Pat implements Command {

    private WeebAPI requester;

    @Inject
    public Pat(WeebAPI requester) {
        this.requester = requester;
    }

    @Override
    public void execute(Message message, String s) {
        String image;
        try {
            image = requester.getRandomImageByType("pat", WeebAPI.NSFW.FALSE, "gif");
        } catch (Exception e) {
            image = "";
            WebHookUtil.err(message.getJDA(), "Error retrieving weeb.sh image!");
            e.printStackTrace();
        }
        if (image.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Something went wrong with fetching the image!").queue();
            return;
        }
        String msg = "***";
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Please @mention somebody to pat.").queue();
            return;
        }
        if (message.getMentionedUsers().size() > 10) {
            message.getChannel().sendMessage(Emote.X + " - You are trying to pat too many people!").queue();
            return;
        }
        if (message.getMentionedUsers().contains(message.getAuthor())) {
            msg += "Diax ";
        } else {
            msg += StringUtil.stripMarkdown(message.getMember().getEffectiveName());
        }
        msg += " is patting " + StringUtil.stripMarkdown(message.getMentionedUsers().stream().map(User::getName).collect(Collectors.joining(", "))) + "***";
        String last = message.getMentionedUsers().get(message.getMentionedUsers().size() - 1).getName();
        if (message.getMentionedUsers().size() > 1) {
            msg = StringUtil.replaceLast(msg, Pattern.quote(", " + last), " and " + last);
        }
        message.getChannel().sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue();
    }
}