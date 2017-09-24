package me.diax.diax.util;

public class StringUtil {

    public static String stripMarkdown(Object input) {
        return (input + "").replaceAll("`", "\\`").replaceAll("\\*", "\\\\*").replaceAll("_", "\\_").replaceAll("~", "\\~");
    }
}