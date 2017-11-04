package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.MUSICAL_NOTE
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message

@CommandDescription(name = "shuffle", triggers = arrayOf("shuffle"))
class Shuffle : Command {

    override fun execute(message: Message, s: String) {
        message.channel.sendMessage(if (GuildMusicManager.getManagerFor(message.guild).scheduler.shuffle(message.textChannel)) "$MUSICAL_NOTE - The queue has been shuffled!" else "$X - Could not shuffle the queue!").queue {}
    }
}