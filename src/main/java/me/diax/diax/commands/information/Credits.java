package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Embed;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "credits",
        triggers = "credits",
        description = "Display the credits for Diax.",
        attributes = @CommandAttribute(key = "private")
)
public class Credits implements Command {

    @Override
    public void execute(Message message, String s) {
        message.getTextChannel().sendMessage(
                Embed.transparent()
                        .addField("comportment#4475", "Owner // Head-Developer", false)
                        .addField("NachtRaben#8307", "Developer // Music", false)
                        .addField("Coolguy3289#2290", "Developer // Website", false)
                        .addField("\uD83D\uDC7Bqu\uD83D\uDC7Brium#9352", "Developer // Website", false)
                        .build()
        ).queue();
    }
}