package me.diax.diax.commands.music;

import me.diax.comportment.jdacommand.Command;

import me.diax.comportment.jdacommand.CommandDescription;
import me.diax.diax.util.Emote;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;

@CommandDescription(
        name = "join",
        triggers = "join"
)
public class Join implements Command {

    @Override
    public void execute(Message message, String s) {
        GuildVoiceState state = message.getMember().getVoiceState();
        TextChannel channel = message.getTextChannel();
        if (!state.inVoiceChannel()) {
            channel.sendMessage(Emote.X + " - You must be in a voice channel to do this!").queue();
            return;
        }
        try {
            message.getGuild().getAudioManager().openAudioConnection(state.getChannel());
            channel.sendMessage(Emote.MUSICAL_NOTE + " - I have joined your voice channel.").queue();
        } catch (PermissionException exception) {
            channel.sendMessage(Emote.X + " - I could not join your voice channel.").queue();
        }
    }
}