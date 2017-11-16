package me.diax.diax.commands.information

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Embed
import me.diax.diax.util.Emote.*
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "credits", triggers = arrayOf("credits"),
        attributes = arrayOf(CommandAttribute(key = "category", value = "information")))
class Credits : Command {

    override fun execute(message: Message, s: String) {
        message.textChannel.sendMessage(
                Embed.themed()
                        .setThumbnail(message.jda.selfUser.effectiveAvatarUrl)
                        .addField("$STAR comportment#4475", "Owner // Head-Developer", false)
                        .addField("$DRAGON_FACE AdrianTodt#0722", "Co-Owner // Backend-Developer", false)
                        .addField("$RAGE Coolguy3289#2290", "Co-Owner // Website-Developer", false)
                        .addField("$MUSICAL_NOTE NachtRaben#8307", "Ex-Developer // Music", false)
                        .build()
        ).queue()
    }
}