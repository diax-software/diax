package me.diax.diax.commands.image

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import me.diax.diax.util.Emote.CAT2
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@CommandDescription(name = "catgirl", triggers = arrayOf("catgirl", "neko", "nya"), attributes = arrayOf(CommandAttribute(key = "private")))
class Catgirl : Command {

    override fun execute(message: Message, s: String) {
        try {
            val json = JSONObject(OkHttpClient().newCall(Request.Builder().url(ENDPOINT).build()).execute().body()!!.string())
            message.channel.sendMessage(Embed.transparent().setTitle("$CAT2 nyaaaa~").setImage(json.getString("neko")).build()).queue()
        } catch (e: Exception) {
            message.channel.sendMessage("$X - Something went wrong fetching a catgirl ;-;").queue()
            message.channel.sendMessage(e.message).queue()
        }
    }

    companion object {
        private val ENDPOINT = "https://nekos.life/api/neko"
    }
}
