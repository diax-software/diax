package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Embed;
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
        message.getChannel().sendMessage(Embed.transparent()
                .addField(
                        "__**Information**__",
                        "I develop Diax in my free time, so do not expect amazing things from him, this is purely for fun. Have fun! - comportment",
                        false)
                .addField(
                        "__**Commands**__",
                        handler.getCommands().stream().map(command -> "`<>" + command.getDescription().name()).collect(Collectors.joining("\n")),
                        false
                ).build()).queue();
    }
}