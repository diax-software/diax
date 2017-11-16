package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.EIGHT_BALL
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(name = "8ball", triggers = arrayOf("8ball", "eightball", "8-ball"), description = "[question]",
        attributes = arrayOf(CommandAttribute(key = "category", value = "fun")))
class EightBall : Command {

    override fun execute(message: Message, s: String) {
        if (s.isBlank() || !s.endsWith("?")) {
            message.channel.sendMessage("$X - Specify a question to ask the 8-ball.").queue()
        } else {
            message.channel.sendMessage("$EIGHT_BALL - " + arrayOf("It is certain", "It is decidedly so", "Without a doubt", "Yes definitely", "You may rely on it", "As I see it, yes", "Most likely", "Outlook good", "Yes", "Signs point to yes", "Reply hazy try again", "Ask again later", "Better not tell you now", "Cannot predict now", "Concentrate and ask again", "Don't count on it", "My reply is no", "My sources say no", "Outlook not so good", "Very doubtful")[Random().nextInt(20)]).queue()
        }
    }
}