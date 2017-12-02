package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandAttribute
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.util.Emote.MUSICAL_NOTE
import me.diax.diax.util.Emote.X
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.exceptions.PermissionException

@CommandDescription(
    name = "join",
    triggers = ["join"],
    attributes = [
        CommandAttribute(key = "category", value = "music")
    ]
)
class Join : Command {

    override fun execute(message: Message, s: String) {
        val state = message.member.voiceState
        val channel = message.textChannel
        if (!state.inVoiceChannel()) {
            channel.sendMessage("$X - You must be in a voice channel to do this!").queue()
            return
        }
        try {
            message.guild.audioManager.openAudioConnection(state.channel)
            channel.sendMessage("$MUSICAL_NOTE - I have joined your voice channel.").queue()
        } catch (exception: PermissionException) {
            channel.sendMessage("$X - I could not join your voice channel.").queue()
        }

    }
}