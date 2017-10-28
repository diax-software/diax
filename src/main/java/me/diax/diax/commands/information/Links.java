package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "links",
        triggers = "links",
        attributes = {
                @CommandAttribute(key = "private")
        }
)
public class Links implements Command {

    @Override
    public void execute(Message trigger, String args) {
        trigger.getChannel().sendMessage(String.join("\n",
                "Invite: <https://discordapp.com/oauth2/authorize?scope=bot&client_id=295500621862404097&permissions=3198016>",
                "Website: <http://diax.me/>",
                "Donate: <https://www.patreon.com/comportment>",
                "Discord Bots: <https://discordbots.org/bot/295500621862404097>",
                "Discord Server: <https://discord.gg/5sJZa2y>"
        )).queue();
    }
}