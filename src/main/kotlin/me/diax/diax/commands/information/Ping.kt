package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import me.diax.diax.util.Emote.*
import net.dv8tion.jda.core.entities.Message
import java.util.*

@CommandDescription(name = "ping", triggers = arrayOf("ping", "pang", "peng", "pong", "pung"), attributes = arrayOf(CommandAttribute(key = "private")))
class Ping : Command {

    override fun execute(message: Message, s: String) {
        val start = System.currentTimeMillis()
        message.channel.sendTyping().queue { _ ->
            val ping = System.currentTimeMillis() - start
            message.channel.sendMessage(Embed.themed().setDescription("$PING_PONG - ***P${arrayOf("a", "e", "i", "o", "u")[Random().nextInt(5)]}ng!***\n${this.pingToEmote(ping)} Response: ${ping}ms\n$BOOKS API: ${message.jda.ping}ms").build()).queue()
        }
    }

    private fun pingToEmote(ping: Long): String {
        if (ping == 69L) return EGGPLANT
        if (ping <= 0) return UPSIDE_DOWN
        if (ping <= 10) return SMILE
        if (ping <= 100) return SMILEY
        if (ping <= 200) return SLIGHT_SMILE
        if (ping <= 300) return NEUTRAL_FACE
        if (ping <= 400) return CONFUSED
        if (ping <= 500) return SLIGHT_FROWN
        if (ping <= 600) return FROWNING2
        if (ping <= 700) return WORRIED
        if (ping <= 800) return DISAPPOINTED
        if (ping <= 900) return SOB
        return FIRE + if (ping <= 1600) "" else FIRE + if (ping <= 10000) "" else FIRE
    }
}