package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.BOOKS
import me.diax.diax.util.Emote.CONFUSED
import me.diax.diax.util.Emote.DISAPPOINTED
import me.diax.diax.util.Emote.EGGPLANT
import me.diax.diax.util.Emote.FIRE
import me.diax.diax.util.Emote.FROWNING2
import me.diax.diax.util.Emote.NEUTRAL_FACE
import me.diax.diax.util.Emote.PING_PONG
import me.diax.diax.util.Emote.SLIGHT_FROWN
import me.diax.diax.util.Emote.SLIGHT_SMILE
import me.diax.diax.util.Emote.SMILE
import me.diax.diax.util.Emote.SMILEY
import me.diax.diax.util.Emote.SOB
import me.diax.diax.util.Emote.UPSIDE_DOWN
import me.diax.diax.util.Emote.WORRIED
import me.diax.diax.util.style.Embed
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(
    name = "ping",
    triggers = ["ping", "pang", "peng", "pong", "pung"]
)
class Ping : Command {

    override fun execute(message: Message, s: String) {
        val start = System.currentTimeMillis()
        message.channel.sendTyping().queue { _ ->
            val ping = System.currentTimeMillis() - start
            message.channel.sendMessage(Embed.themed().setDescription("$PING_PONG - ***P${arrayOf("a", "e", "i", "o", "u")[Random().nextInt(5)]}ng!***\n${this.pingToEmote(ping)} Response: ${ping}ms\n$BOOKS API: ${message.jda.ping}ms").build()).queue()
        }
    }

    private fun pingToEmote(ping: Long): String {
        return when {
            ping == 69L -> EGGPLANT
            ping <= 0 -> UPSIDE_DOWN
            ping <= 10 -> SMILE
            ping <= 100 -> SMILEY
            ping <= 200 -> SLIGHT_SMILE
            ping <= 300 -> NEUTRAL_FACE
            ping <= 400 -> CONFUSED
            ping <= 500 -> SLIGHT_FROWN
            ping <= 600 -> FROWNING2
            ping <= 700 -> WORRIED
            ping <= 800 -> DISAPPOINTED
            ping <= 900 -> SOB
            ping <= 1600 -> FIRE
            ping <= 10000 -> FIRE * 2
            else -> FIRE * 3
        }
    }

    private operator fun String.times(i: Int): String {
        val b = StringBuilder()
        for (any in 0..i) b.append(this)
        return b.toString()
    }
}
