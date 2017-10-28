package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Data;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "reload",
        triggers = "reload",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Reload implements Command {

    private Data data;

    public Reload(Data data) {
        this.data = data;
    }

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Emote.SMILE + " - Reloading data...").queue();
        try {
            data.reloadData();
        } catch (Exception e) {
            message.getChannel().sendMessage(Emote.X + " - Error reloading data!").queue();
        }
    }
}