package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote
import net.dv8tion.jda.core.entities.Message

@CommandDescription(
    name = "skip",
    triggers = ["skip"]
)
class Skip : Command {

    override fun execute(message: Message, s: String) {
        if (!GuildMusicManager.getManagerFor(message.guild).scheduler.isPlaying(message.textChannel)) {
            message.channel.sendMessage(Emote.X + " - There is nothing playing!").queue()
            return
        }
        message.textChannel.sendMessage(Emote.MUSICAL_NOTE + " - Skipping the current track.").queue()
        GuildMusicManager.getManagerFor(message.guild).scheduler.skip(message.textChannel)
    }
}