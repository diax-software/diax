package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Embed;
import net.dv8tion.jda.core.entities.Message;

import java.util.stream.Collectors;

@CommandDescription(
        name = "help",
        triggers = "help",
        description = "Displays the help message for Diax.",
        attributes = @CommandAttribute(key = "private")
)
public class Help implements Command {

    private CommandHandler handler;
    private String prefix;

    public Help(CommandHandler handler, String prefix) {
        this.handler = handler;
        this.prefix = prefix;
    }

    @Override
    public void execute(Message message, String s) {
        message.getChannel().sendMessage(Embed.transparent()
                .addField(
                        "__**Commands**__",
                        handler.getCommands().stream().filter(command -> !command.hasAttribute("hidden")).sorted().map(command -> "`" + prefix + command.getDescription().name() + " | " + command.getDescription().description() + "`").collect(Collectors.joining("\n")),
                        false
                )
                .addField("__**Links**__", String.join("\n",
                        "[Patreon](https://patreon.com/comportment) - Donate here to help support us!",
                        "[Discord](https://discord.gg/5sJZa2y) - Come here to chat or for help!",
                        "[Website](http://diax.me) - Check out our website!"
                        ), false
                ).build()).queue();
    }
}