package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.data.config.entities.Config;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import javax.inject.Inject;

@CommandDescription(
        name = "blacklist",
        triggers = "blacklist",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Blacklist implements Command {

    private Config config;

    @Inject
    public Blacklist(Config config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String s) {
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Mention a user to (un)blacklist.").queue();
        }
        String id = message.getMentionedUsers().get(0).getId();
        if (config.getBlacklist().contains(id)) {
            config.getBlacklist().remove(id);
        } else {
            config.getBlacklist().add(id);
        }
        message.getChannel().sendMessage(Emote.SMILE + " - " + message.getMentionedUsers().get(0).getName() + " has been " + (config.getBlacklist().contains(id) ? "added to" : "removed from") + " the blacklist.").queue();
    }
}