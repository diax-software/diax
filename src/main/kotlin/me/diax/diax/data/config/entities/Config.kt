package me.diax.diax.data.config.entities

import java.util.*

class Config {
    var prefix: String? = null
    var type: String? = null

    var tokens: Tokens = Tokens()
    var channels: Channels = Channels()
    var database: DbConfig = DbConfig()

    var developers: MutableList<String> = LinkedList()
    var donors: MutableList<String> = LinkedList()
    var blacklist: MutableList<String> = LinkedList()

    fun addDeveloper(id: String) {
        developers.add(id)
    }

    fun removeDeveloper(id: String) {
        developers.remove(id)
    }

    fun addDonor(id: String) {
        donors.add(id)
    }

    fun removeDonor(id: String) {
        donors.remove(id)
    }

    fun blacklist(id: String) {
        blacklist.add(id)
    }

    fun unBlacklist(id: String) {
        blacklist.remove(id)
    }

    override fun toString(): String =
        "Config(prefix=$prefix, type=$type, tokens=$tokens, channels=$channels, database=$database, developers=$developers, donors=$donors, blacklist=$blacklist)"
}