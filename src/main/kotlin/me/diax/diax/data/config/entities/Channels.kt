package me.diax.diax.data.config.entities

class Channels {
    var bugs: String? = null
    var logging: String? = null
    var suggestions: String? = null
    var announcements: String? = null
    var output: String? = null

    override fun toString(): String =
            "Channels(bugs=$bugs, logging=$logging, suggestions=$suggestions, announcements=$announcements, output=$output)"
}
