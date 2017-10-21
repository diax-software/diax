package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandAttribute;
import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.music.GuildMusicManager;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.Message;

@CommandDescription(
        name = "volume",
        triggers = "volume",
        attributes = @CommandAttribute(key = "patreon"),
        description = "[0-150] | Sets the volume of Diax."
)
public class Volume implements Command {

    @Override
    public void execute(Message trigger, String truncated) {
        try {
            int volume = Integer.valueOf(truncated.split("\\s+")[0]);
            if (volume > 150 || volume < 0) throw new NumberFormatException("Invalid number in expected range.");
            GuildMusicManager.getManagerFor(trigger.getGuild()).getPlayer().setVolume(volume);
            trigger.getChannel().sendMessage(Emote.MUSICAL_NOTE + " - The volume has been set to: (" + volume + "/150)").queue();
        } catch (NumberFormatException e) {
            trigger.getChannel().sendMessage(Emote.X + " - That is not a valid number (0-150)!").queue();
        }
    }
}