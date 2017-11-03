package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.stream.Collectors;

@CommandDescription(
        name = "kiss",
        triggers = "kiss"
)
public class Kiss implements Command {

    private WeebAPI requester;

    public Kiss(WeebAPI requester) {
        this.requester = requester;
    }

    @Override
    public void execute(Message message, String s) {
        String image;
        try {
            image = requester.getRandomImageByType("kiss", WeebAPI.NSFW.FALSE, "gif");
        } catch (Exception e) {
            image = "";
            WebHookUtil.err(message.getJDA(), "Error retrieving weeb.sh image!");
        }
        if (image.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Something went wrong with fetching the image!").queue();
            return;
        }
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Please @mention somebody to kiss.").queue();
            return;
        }
        if (message.getMentionedUsers().size() > 1) {
            message.getChannel().sendMessage(Emote.X + " - You can't kiss more than one person at a time you baka!").queue();
            return;
        }
        String msg = "***";
        msg += StringUtil.stripMarkdown(message.getMember().getEffectiveName()) + " is kissing " + StringUtil.stripMarkdown(message.getMentionedUsers().stream().map(User::getName).collect(Collectors.joining(", "))) + "***";
        message.getChannel().sendMessage(Embed.transparent().setDescription(msg).setImage(image).build()).queue();
    }
}