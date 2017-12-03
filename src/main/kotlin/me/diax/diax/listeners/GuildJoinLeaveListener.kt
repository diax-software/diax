package me.diax.diax.listeners

import me.diax.diax.util.JDAUtil
import me.diax.diax.util.StringUtil
import me.diax.diax.util.WebHookUtil
import net.dv8tion.jda.core.events.guild.GenericGuildEvent
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class GuildJoinLeaveListener(private val auth: String) : ListenerAdapter() {

    override fun onGuildJoin(event: GuildJoinEvent) {
        this.onLeaveOrJoin(event)
        WebHookUtil.log(event.jda, "Joined Guild:", "```" + StringUtil.stripMarkdown(event.guild.toString() + " | +" + event.guild.members.size + " members. | Guilds: " + event.jda.guilds.size) + "```")
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        this.onLeaveOrJoin(event)
        WebHookUtil.log(event.jda, "Left Guild:", "```" + StringUtil.stripMarkdown(event.guild.toString() + " | -" + event.guild.members.size + " members. | Guilds: " + event.jda.guilds.size) + "```")
    }

    private fun onLeaveOrJoin(event: GenericGuildEvent) {
        JDAUtil.sendGuilds(event.jda, auth)
    }
}