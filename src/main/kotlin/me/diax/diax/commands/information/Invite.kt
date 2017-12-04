package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
    name = "invite",
    triggers = ["invite"]
)
class Invite : Command {

    override fun execute(trigger: Message, args: String) {
        trigger.channel.sendMessage("https://discordapp.com/oauth2/authorize?scope=bot&client_id=295500621862404097&permissions=3198016").queue()
    }
}