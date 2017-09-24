package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Embed;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

import java.util.stream.Collectors;

@CommandDescription(
    name = "help",
    triggers = "help"
)
public class Help implements Command {

    private CommandHandler handler;

    public Help(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Embed.transparent().addField(
                "__**Commands**__",
                handler.getCommands().stream().map(command -> "`<>" + command.getDescription().name() + "` | `" +  command.getDescription() + "`").collect(Collectors.joining("\n")),
                false
        ).build()).queue();
    }
}