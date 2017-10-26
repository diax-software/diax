package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Data;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "shutdown",
        triggers = "shutdown",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Shutdown implements Command {

    private Data data;

    public Shutdown(Data data) {
        this.data = data;
    }

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Emote.ZZZ + " - Shutting down...").queue();
        data.addDeveloper("101");
        try {
            data.saveData();
        } catch (Exception e) {
            message.getChannel().sendMessage(Emote.X + " - Error saving data.").queue();
        }
        System.exit(-1);
    }
}