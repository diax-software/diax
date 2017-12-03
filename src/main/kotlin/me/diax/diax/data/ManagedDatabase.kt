package me.diax.diax.data

import com.google.inject.Inject
import com.rethinkdb.RethinkDB.r
import com.rethinkdb.pool.ConnectionPool
import me.diax.diax.data.db.GuildData
import me.diax.diax.data.db.UserData
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User

class ManagedDatabase
@Inject constructor(private val pool: ConnectionPool) {

    operator fun get(guild: Guild): GuildData {
        return getGuild(guild.id)
    }

    operator fun get(user: User): UserData {
        return getUser(user.id)
    }

    operator fun get(guild: Guild, create: Boolean): GuildData? {
        return getGuild(guild.id, create)
    }

    operator fun get(user: User, create: Boolean): UserData? {
        return getUser(user.id, create)
    }

    fun getGuild(id: String): GuildData {
        return getGuild(id, true)!!
    }

    fun getGuild(id: String, create: Boolean): GuildData? {
        val guild: GuildData? = pool.run(r.table("users").get(id), UserData::class.java, { null })
        return if (guild == null && create) GuildData() else guild
    }

    fun getUser(id: String): UserData {
        return getUser(id, true)!!
    }

    fun getUser(id: String, create: Boolean): UserData? {
        val user: UserData? = pool.run(r.table("users").get(id), UserData::class.java, { null })
        return if (user == null && create) UserData() else user
    }
}
