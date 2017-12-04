package me.diax.diax.data.db.extra

class GuildSettings {
    var isCategories: Boolean = false
    var prefix: String? = null

    override fun toString(): String = "GuildSettings(isCategories=$isCategories)"
}
