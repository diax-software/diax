package me.diax.diax.util

import br.com.brjdevs.java.utils.collections.CollectionUtils
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.*
import java.util.regex.Pattern

private val userMention = Pattern.compile("<@!?\\d+>")

val Channel.mention: String
    get() = when (this.type) {
        ChannelType.TEXT -> (this as TextChannel).asMention
        ChannelType.PRIVATE -> (this as PrivateChannel).user.asMention + "'s DM"
        ChannelType.GROUP -> this.name
        else -> throw IllegalStateException("[JDA Error] What in the Name of fuck.")
    }

fun String.usersMentioned(jda: JDA): List<User> {
    return CollectionUtils
            .iterable(userMention, this)
            .mapNotNull {
                try {
                    jda.getUserById(it.substring(if (it.startsWith("<@!")) 3 else 2, it.length - 1))
                } catch (_: Exception) {
                    null
                }
            }
            .toList()
}

fun User.game(): Game? {
    val mutualGuilds = jda.getMutualGuilds(this)
    if (mutualGuilds.isEmpty()) return null
    return mutualGuilds[0].getMember(this).game
}

fun User.name(member: Member?): String {
    return if (member != null) member.effectiveName else name
}

fun User.name(guild: Guild? = null): String {
    return if (guild != null && guild.isMember(this)) guild.getMember(this).effectiveName else name
}

fun User.onlineStatus(): OnlineStatus? {
    val mutualGuilds = jda.getMutualGuilds(this)
    if (mutualGuilds.isEmpty()) return null
    return mutualGuilds[0].getMember(this).onlineStatus
}
