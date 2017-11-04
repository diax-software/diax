package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.BUTTON
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(name = "flip", triggers = arrayOf("flip", "coin", "heads", "toss", "tails", "hot"), attributes = arrayOf(CommandAttribute(key = "private")))
class Flip : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage("$BUTTON - You flipped a " + (if (Random().nextInt(2) == 1) "heads" else "tails") + ".").queue()
    }
}