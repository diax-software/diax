package me.diax.diax.setup

import com.rethinkdb.RethinkDB.r
import com.rethinkdb.get
import com.rethinkdb.net.Connection
import me.diax.diax.data.config.ConfigManager

fun connect(): Connection = ConfigManager().load().database.configure().connect()

fun main(args: Array<String>) {
    //Holds possible Migration tools
    val map = mutableMapOf<String, () -> Unit>(
            "setup" to ::setup
    )

    map.put("help") {
        println("Diax - RethinkDB setup")
        println("Arguments available:")
        println(map.keys.joinToString(prefix = "\t", separator = " "))
    }

    map[if (map.containsKey(args[0])) args[0] else "help"]!!()
}

fun setup() {
    val conn = connect()

    arrayOf("users", "guilds", "commands").forEach {
        r.tableCreate(it).run(conn)
    }

    r.table("commands")
            .indexCreate("guildId")
        .run<Any>(conn)

    r.table("commands")
        .indexCreate("guild_name") { row -> r.array(row["guildId"], row["name"]) }
            .run<Any>(conn)
}