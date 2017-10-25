package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.util.Emote;
import me.diax.diax.util.StringUtil;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandDescription(
        name = "queue",
        triggers = "queue",
        description = "Displays the songs in the queue."
)
public class Queue implements Command {

    @Override
    public void execute(Message message, String s) {
        GuildMusicManager manager = GuildMusicManager.getManagerFor(message.getGuild());
        List<MusicTrack> queue = new ArrayList<>(manager.getScheduler().getQueue());
        queue.add(0, manager.getScheduler().getCurrentTrack());
        String msg;
        if (queue.isEmpty() || manager.getScheduler().getCurrentTrack() == null) {
            msg = Emote.MUSICAL_NOTE + " - The queue is empty.";
        } else {
            List<MusicTrack> reducedQueue = queue.subList(0, queue.size() > 10 ? 10 : queue.size());
            msg = reducedQueue.stream().map(track -> "` (" + (reducedQueue.indexOf(track) + 1) + ") " + StringUtil.stripMarkdown(track.getTrack().getInfo().title) + " | " + StringUtil.stripMarkdown(track.getRequester().getNickname()) + "#" + track.getRequester().getUser().getDiscriminator() + "`").collect(Collectors.joining("\n", "Queue:\n\n", String.format("\n\nShowing (%s/%s) items.", reducedQueue.size(), queue.size())));
        }
        message.getChannel().sendMessage(msg).queue();
    }
}