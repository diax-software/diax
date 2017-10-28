package me.diax.diax.commands.information;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "invite",
        triggers = "invite",
        attributes = {
                @CommandAttribute(key = "private")
        }
)
public class Invite implements Command {

    @Override
    public void execute(Message trigger, String args) {
        trigger.getChannel().sendMessage(trigger.getJDA().asBot().getInviteUrl(Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES)).queue();
    }
}