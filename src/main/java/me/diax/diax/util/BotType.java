package me.diax.diax.util;

import lombok.Getter;

import java.awt.Color;

public enum BotType {
    STABLE(Colors.BURPLE),

    PATREON(Colors.CANARY),

    DEV(Colors.GREY);

    public static BotType CURRENT_TYPE = STABLE;

    @Getter
    private final Color mainColor;

    BotType(Color mainColor) {
        this.mainColor = mainColor;
    }
}
