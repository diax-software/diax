package me.diax.diax.data

import com.google.inject.Inject
import com.rethinkdb.RethinkDB.r
import com.rethinkdb.net.Cursor
import com.rethinkdb.onIndex
import com.rethinkdb.pool.ConnectionPool
import com.rethinkdb.run
import me.diax.diax.data.db.CustomCommand
import me.diax.diax.data.db.GuildData
import me.diax.diax.data.db.UserData
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.User
import java.util.function.Function

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
        val guild: GuildData? = r.table("guilds")
            .get(id)
            .run(pool, GuildData::class.java, Function { null })

        return if (guild == null && create) GuildData() else guild
    }

    fun getUser(id: String): UserData {
        return getUser(id, true)!!
    }

    fun getUser(id: String, create: Boolean): UserData? {
        val user: UserData? = r.table("users")
            .get(id)
            .run(pool, UserData::class.java, Function { null })

        return if (user == null && create) UserData() else user
    }

    fun getCustomCommand(id: String): CustomCommand? = r.table("commands").get(id).run(pool, CustomCommand::class.java, Function { null })

    fun getCustomCommand(guildId: String, name: String): CustomCommand? {
        val cursor: Cursor<CustomCommand>? = r.table("commands")
            .getAll(r.array(guildId, name))
            .onIndex("guild_name")
            .run(pool, CustomCommand::class.java, Function { null })

        return cursor?.use {
            if (cursor.hasNext()) null else cursor.next()
        }
    }
}
