package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote
import me.diax.diax.util.Emote.BOOKS
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(name = "ping", triggers = arrayOf("ping", "pang", "peng", "pong", "pung"), attributes = arrayOf(CommandAttribute(key = "private")))
class Ping : Command {

    override fun execute(message: Message, s: String) {
        val start = System.currentTimeMillis()
        message.channel.sendTyping().queue { _ ->
            val ping = System.currentTimeMillis() - start
            message.channel.sendMessage(Emote.PING_PONG + " - ***P${arrayOf("a", "e", "i", "o", "u")[Random().nextInt(5)]}ng!***\n${this.pingToEmote(ping)} Response: ${ping}ms\n$BOOKS API: ${message.jda.ping}ms").queue()
        }
    }

    fun pingToEmote(ping: Long): String {
        if (ping == 69L) return Emote.EGGPLANT
        if (ping <= 0) return Emote.UPSIDE_DOWN
        if (ping <= 10) return Emote.SMILE
        if (ping <= 100) return Emote.SMILEY
        if (ping <= 200) return Emote.SLIGHT_SMILE
        if (ping <= 300) return Emote.NEUTRAL_FACE
        if (ping <= 400) return Emote.CONFUSED
        if (ping <= 500) return Emote.SLIGHT_FROWN
        if (ping <= 600) return Emote.FROWNING2
        if (ping <= 700) return Emote.WORRIED
        if (ping <= 800) return Emote.DISAPPOINTED
        if (ping <= 900) return Emote.SOB
        if (ping <= 1600) return Emote.FIRE
        return if (ping <= 10000) Emote.FIRE + Emote.FIRE else Emote.FIRE + Emote.FIRE + Emote.FIRE
    }
}