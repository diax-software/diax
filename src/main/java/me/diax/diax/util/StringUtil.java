package me.diax.diax.util;

import java.net.URLEncoder;
import java.util.Map;

public class StringUtil {

    public static String stripMarkdown(Object input) {
        return (input + "").replaceAll("`", "\\`").replaceAll("\\*", "\\\\*").replaceAll("_", "\\_").replaceAll("~", "\\~").replaceAll("@everyone", "everyone").replaceAll("@here", "here");
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        System.out.println(map);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return "error";
        }
    }
}