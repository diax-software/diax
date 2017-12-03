package me.diax.diax.data.config.entities

import com.rethinkdb.RethinkDB.r
import com.rethinkdb.net.Connection.Builder

class DbConfig {
    var hostname: String? = null
    var dbname: String? = null
    var port: Int? = null
    var user: String? = null
    var password: String? = null

    fun configure(): Builder {
        val b = r.connection()
        if (hostname != null) b.hostname(hostname!!)
        if (hostname != null) b.db(dbname!!)
        if (port != null) b.port(port!!)
        if (user != null) b.user(user!!, password!!)
        return b
    }

    override fun toString(): String =
        "DbConfig(hostname=$hostname, dbname=$dbname, port=$port, user=$user, password=$password)"
}
