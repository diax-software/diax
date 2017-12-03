package me.diax.diax.data.db

import me.diax.diax.data.db.extra.GuildProfile
import me.diax.diax.data.db.extra.GuildSettings

class GuildData {
    var id: String? = null
    var settings: GuildSettings = GuildSettings()
    var profile: GuildProfile = GuildProfile()

    override fun toString(): String = "GuildData(id=$id, settings=$settings, profile=$profile)"
}
