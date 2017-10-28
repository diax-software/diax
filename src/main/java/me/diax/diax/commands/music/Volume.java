package me.diax.diax.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "volume",
        triggers = {
                "volume",
                "vol"
        },
        attributes = @CommandAttribute(key = "patreon"),
        description = "{0-150}"
)
public class Volume implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
        AudioPlayer player = GuildMusicManager.getManagerFor(trigger.getGuild()).getPlayer();
        if (truncated.isEmpty()) {
            trigger.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - The volume is [" + player.getVolume() + "/150]").queue();
            return;
        }
        try {
            int volume = Integer.valueOf(truncated.split("\\s+")[0]);
            if (volume > 150 || volume < 0) throw new NumberFormatException("Invalid number in expected range.");
            player.setVolume(volume);
            trigger.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - The volume has been set to [" + volume + "/150]").queue();
        } catch (NumberFormatException e) {
            trigger.getChannel().sendMessage(Emote.X + " - That is not a valid number [0/150]!").queue();
        }
    }
}