package me.diax.diax.commands.`fun`

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.commands.`fun`.helper.DiceEvaluator
import me.diax.diax.util.Emote
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(
    name = "die",
    triggers = ["die", "dice", "roll"],
    description = "{number/dice notation}"
)
class Die : Command {
    private val random: Random = Random()

    override fun execute(message: Message, s: String) {
        if (s.isEmpty()) {
            message.channel.sendMessage(Emote.GAME_DIE + " - You rolled a `" + (random.nextInt(6) + 1) + "`").queue()
            return
        }

        try {
            message.channel.sendMessage(Emote.GAME_DIE + " - You rolled a `" + DiceEvaluator(s).parse() + "`").queue()
        } catch (_: Exception) {
            message.channel.sendMessage(Emote.X + " - Invalid dice roll!\nExamples: \n```" +
                "Examples:\n" +
                "<>roll 1\n" +
                "<>roll 2d10\n" +
                "<>roll 1d4+20\n" +
                "<>roll 1d5-2```"
            ).queue()
        }
    }
}