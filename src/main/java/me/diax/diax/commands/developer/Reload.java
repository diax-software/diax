package me.diax.diax.commands.developer;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.data.config.ConfigManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import javax.inject.Inject;

@CommandDescription(
        name = "reload",
        triggers = "reload",
        attributes = {
                @CommandAttribute(key = "developer"),
                @CommandAttribute(key = "hidden")
        }
)
public class Reload implements Command {
    private final ConfigManager manager;

    @Inject
    public Reload(ConfigManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Emote.SMILE + " - Reloading data...").queue();
        try {
            manager.load();
        } catch (Exception e) {
            message.getChannel().sendMessage(Emote.X + " - Error reloading data!").queue();
        }
    }
}