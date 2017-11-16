package me.diax.diax.commands.music

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.diax.music.GuildMusicManager
import me.diax.diax.util.Emote.MUSICAL_NOTE
import me.diax.diax.util.StringUtil
import net.dv8tion.jda.core.entities.Message
import java.util.*
import java.util.stream.Collectors

@CommandDescription(name = "queue", triggers = arrayOf("queue"))
class Queue : Command {

    override fun execute(message: Message, s: String) {
        val manager = GuildMusicManager.getManagerFor(message.guild)
        val queue = ArrayList(manager.scheduler.queue)
        queue.add(0, manager.scheduler.currentTrack)
        val msg: String
        when (queue.isEmpty() || manager.scheduler.currentTrack == null) {
            true -> msg = "The queue is empty."
            false -> {
                val reducedQueue = queue.subList(0, if (queue.size > 10) 10 else queue.size)
                msg = reducedQueue.stream().map { track -> "` (${if (reducedQueue.indexOf(track) == 9) "" else "0"}${reducedQueue.indexOf(track) + 1}) ${StringUtil.stripMarkdown(track.track.info.title)} | ${StringUtil.stripMarkdown(track.track.info.author)} | ${StringUtil.stripMarkdown(track.requester.effectiveName)}#${track.requester.user.discriminator} `" }.collect(Collectors.joining("\n", "Queue:\n\n", String.format("\n\nShowing (%s/%s) items.", reducedQueue.size, queue.size)))
            }
        }
        message.channel.sendMessage("$MUSICAL_NOTE - $msg").queue()
    }
}