package me.diax.diax.util.style

import java.awt.Color

enum class BotType constructor(val mainColor: Color) {
    STABLE(Colors.BURPLE),
    PATREON(Colors.CANARY),
    DEV(Colors.DARK);

    companion object {
        var CURRENT_TYPE = STABLE
    }
}
