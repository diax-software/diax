package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.music.MusicTrack;
import me.diax.diax.util.StringUtil;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandDescription(
        name = "queue",
        triggers = "queue",
        description = "Displays the track queue."
)
public class Queue implements Command {

    @Override
    public void execute(Message message, String s) {
        GuildMusicManager manager = GuildMusicManager.getManagerFor(message.getGuild());
        List<MusicTrack> queue = new ArrayList<>(manager.getScheduler().getQueue());
        queue.add(manager.getScheduler().getCurrentTrack());
        String msg;
        if (queue.isEmpty()) {
            msg = "The queue is empty.";
        } else {
            msg = queue.stream().map(track -> "`" + StringUtil.stripMarkdown(track.getTrack().getInfo().title) + " | " + StringUtil.stripMarkdown(track.getRequester().getNickname()) + "#" + track.getRequester().getUser().getDiscriminator() + "`").collect(Collectors.joining("\n", "Queue:\n\n", ""));
        }
        message.getChannel().sendMessage(msg).queue();
    }
}