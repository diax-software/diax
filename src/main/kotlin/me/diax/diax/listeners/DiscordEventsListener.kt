package me.diax.diax.listeners

import com.rethinkdb.pool.ConnectionPool
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import javax.inject.Inject

class DiscordEventsListener
@Inject constructor(
    val pool: ConnectionPool
) : ListenerAdapter() {

    override fun onGuildLeave(event: GuildLeaveEvent) {
//        val guildId = event.guild.id
//
//        //In later versions, we could delegate this to a external service to do that in a queue fashion with timeout
//        r.table("guilds")
//            .get(guildId)
//            .delete()
//            .run<Any>(pool)
//
//        r.table("commands")
//            .getAll(guildId)
//            .onIndex("guildId")
//            .delete()
//            .run<Any>(pool)
    }
}