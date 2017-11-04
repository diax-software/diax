package me.diax.diax.util;

import lombok.Getter;

import java.awt.Color;

public enum BotType {
    STABLE(new Color(0x7289DA)),

    PATREON(new Color(0xF5B249)),

    DEV(new Color(0x23272A));

    public static BotType CURRENT_TYPE = STABLE;

    @Getter
    private final Color mainColor;

    BotType(Color mainColor) {
        this.mainColor = mainColor;
    }
}
