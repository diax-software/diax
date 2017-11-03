package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import javax.inject.Inject;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CommandDescription(
        name = "hug",
        triggers = "hug"
)
public class Hug implements Command {

    private WeebAPI requester;

    @Inject
    public Hug(WeebAPI requester) {
        this.requester = requester;
    }

    @Override
    public void execute(Message message, String s) {
        String image;
        try {
            image = requester.getRandomImageByType("hug", WeebAPI.NSFW.FALSE, "gif");
        } catch (Exception e) {
            image = "";
            WebHookUtil.err(message.getJDA(), "Error retrieving weeb.sh image!");
        }
        if (image.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Something went wrong with fetching the image!").queue();
            return;
        }
        String msg = "***";
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Please @mention somebody to hug.").queue();
            return;
        }
        if (message.getMentionedUsers().size() > 10) {
            message.getChannel().sendMessage(Emote.X + " - You are trying to hug too many people!").queue();
            return;
        }
        if (message.getMentionedUsers().contains(message.getAuthor())) {
            msg += "Diax ";
        } else {
            msg += StringUtil.stripMarkdown(message.getMember().getEffectiveName());
        }
        msg += " is hugging " + StringUtil.stripMarkdown(message.getMentionedUsers().stream().map(User::getName).collect(Collectors.joining(", "))) + "***";
        String last = message.getMentionedUsers().get(message.getMentionedUsers().size() - 1).getName();
        if (message.getMentionedUsers().size() > 1) {
            msg = StringUtil.replaceLast(msg, Pattern.quote(", " + last), " and " + last);
        }
        message.getChannel().sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue();
    }
}