package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.Permission;
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
                "Invite: <" + trigger.getJDA().asBot().getInviteUrl(Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES) + ">",
                "Website: <http://diax.me/>",
                "Donate: <https://www.patreon.com/comportment>",
                "Discord Bots: <https://discordbots.org/bot/295500621862404097>",
                "Discord Server: <https://discord.gg/5sJZa2y>"
        )).queue();
    }
}