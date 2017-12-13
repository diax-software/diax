package me.diax.diax.util

import java.net.URLEncoder

object StringUtil {

    fun stripMarkdown(input: Any): String {
        return (input.toString() + "").replace("`".toRegex(), "\\`").replace("\\*".toRegex(), "\\\\*").replace("_".toRegex(), "\\_").replace("~".toRegex(), "\\~").replace("@everyone".toRegex(), "everyone").replace("@here".toRegex(), "here")
    }

    fun replaceLast(text: String, regex: String, replacement: String): String {
        return text.replaceFirst("(?s)$regex(?!.*?$regex)".toRegex(), replacement)
    }

    fun urlEncodeUTF8(map: Map<*, *>): String {
        println(map)
        val sb = StringBuilder()
        for ((key, value) in map) {
            if (sb.length > 0) {
                sb.append("&")
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(key.toString()),
                    urlEncodeUTF8(value.toString())
            ))
        }
        return sb.toString()
    }

    private fun urlEncodeUTF8(s: String): String {
        try {
            return URLEncoder.encode(s, "UTF-8")
        } catch (e: Exception) {
            return "error"
        }

    }
}