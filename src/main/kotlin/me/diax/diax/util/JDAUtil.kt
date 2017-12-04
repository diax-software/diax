package me.diax.diax.util

import com.github.natanbc.discordbotsapi.DiscordBotsAPI
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object JDAUtil {

    private var API: DiscordBotsAPI? = null

    private fun getStatus(jda: JDA): String {
        val status = arrayOf("Guilds: (guilds) | Users: (users)", "Invite: https://discord.gg/5sJZa2y", "Donate: https://patreon.com/comportment")
        return "help | " + status[Random().nextInt(status.size)]
            .replace("\\(guilds\\)".toRegex(), jda.guilds.size.toString() + "")
            .replace("\\(users\\)".toRegex(), jda.users.size.toString() + "")
    }

    fun startGameChanging(jda: JDA, prefix: String) {
        val executor = Executors.newSingleThreadScheduledExecutor()
        val periodicTask = {
            var status = jda.presence.game.name
            while (status == jda.presence.game.name) {
                status = JDAUtil.getStatus(jda)
            }
            jda.presence.game = Game.playing(prefix + status)
            jda.presence.status = OnlineStatus.ONLINE
        }
        executor.scheduleAtFixedRate(periodicTask, 0, 20, TimeUnit.SECONDS)
    }

    fun sendGuilds(jda: JDA, botToken: String?) {
        if (botToken == null || botToken.isEmpty()) return
        if (API == null) API = DiscordBotsAPI(botToken)
        try {
            API!!.postStats(1, 0, jda.guilds.size)
        } catch (e: Exception) {
            WebHookUtil.err(jda, "Couldn't update bot list stats.")
        }

    }
}