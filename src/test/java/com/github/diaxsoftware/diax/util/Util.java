package com.github.diaxsoftware.diax.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.Arrays;

public class Util {

    public static class Colour {
        public static Color BLACKGROUND = new Color(0x202225);
        public static Color BLURPLE = new Color(0x7289DA);
        public static Color DAMARED = new Color(0xb64240);
    }

    public static void sendMessage(MessageChannel channel, String message, Color color, Tuple<String, String[]>... sections) {
        EmbedBuilder eb = new EmbedBuilder().setColor(color).setDescription(message);
        if (sections != null) {
            Arrays.stream(sections).forEach(section -> eb.addField(section.a, String.join("\n", section.b), true));
            for (Tuple<String, String[]> section : sections) {
                eb.addField(section.a, String.join("\n", section.b), true);
            }
        }
        channel.sendMessage(eb.build()).queue();
    }

    public static void sendMessage(MessageChannel channel, String message, Tuple<String, String[]>... sections) {
        sendMessage(channel, message, Colour.BLURPLE, sections);
    }

    public static void sendMessage(MessageChannel channel, String message, Color color) {
        sendMessage(channel, message, color, null);
    }
    public static void sendMessage(MessageChannel channel, String message) {
        sendMessage(channel, message, Colour.BLURPLE);
    }

    public static void sendError(MessageChannel channel, String message) {
        sendMessage(channel, message, Colour.DAMARED);
    }

    public static void sendError(MessageChannel channel) {
        sendError(channel, "Error!");
    }
}
