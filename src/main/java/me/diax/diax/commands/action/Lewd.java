package me.diax.diax.commands.action;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import me.diax.diax.util.WeebAPI;
import net.dv8tion.jda.core.entities.Message;

import javax.inject.Inject;

@CommandDescription(
        name = "lewd",
        triggers = "lewd"
)
public class Lewd implements Command {

    private WeebAPI requester;

    @Inject
    public Lewd(WeebAPI requester) {
        this.requester = requester;
    }

    @Override
    public void execute(Message message, String s) {
        String image;
        try {
            image = requester.getRandomImageByType("lewd", WeebAPI.NSFW.FALSE);
        } catch (Exception e) {
            image = "";
            WebHookUtil.err(message.getJDA(), "Error retrieving weeb.sh image!");
        }
        if (image.isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Something went wrong with fetching the image!").queue();
            return;
        }
        message.getChannel().sendMessage(Embed.themed().setDescription("***Too lewd!***").setImage(image).build()).queue();

    }
}