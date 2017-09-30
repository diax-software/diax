package me.diax.diax.util;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class Embed {

    public static EmbedBuilder transparent() {
        return new EmbedBuilder().setColor(new Color(54, 57, 62));
    }
}