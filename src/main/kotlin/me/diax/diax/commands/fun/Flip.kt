package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.DIAXCOIN_HEADS
import me.diax.diax.util.Emote.DIAXCOIN_TAILS
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(
    name = "flip",
    triggers = ["flip", "coin", "heads", "toss", "tails", "hot"]
)
class Flip : Command {
    val results = arrayOf("$DIAXCOIN_HEADS - You flipped a heads.", "$DIAXCOIN_TAILS - You flipped a tails.")

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage(results[Random().nextInt(1)]).queue()
    }
}