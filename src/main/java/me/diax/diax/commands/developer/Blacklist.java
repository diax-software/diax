package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Data;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "blacklist",
        triggers = "blacklist",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Blacklist implements Command {

    private Data data;

    public Blacklist(Data data) {
        this.data = data;
    }

    @Override
    public void execute(Message message, String s) {
        if (message.getMentionedUsers().isEmpty()) {
            message.getChannel().sendMessage(Emote.X + " - Mention a user to (un)blacklist.").queue();
        }
        String id = message.getMentionedUsers().get(0).getId();
        if (data.getBlacklist().contains(id)) {
            data.getBlacklist().remove(id);
        } else {
            data.getBlacklist().add(id);
        }
        message.getChannel().sendMessage(Emote.SMILE + " - " + message.getMentionedUsers().get(0).getName() + " has been " + (data.getBlacklist().contains(id) ? "added to" : "removed from") + " the blacklist.").queue();
    }
}