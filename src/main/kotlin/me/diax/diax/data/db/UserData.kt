package me.diax.diax.data.db

import me.diax.diax.data.db.extra.UserProfile
import me.diax.diax.data.db.extra.UserSettings

class UserData {
    var id: String? = null
    var settings: UserSettings = UserSettings()
    var profile: UserProfile = UserProfile()

    override fun toString(): String = "UserData(id=$id, settings=$settings, profile=$profile)"
}
