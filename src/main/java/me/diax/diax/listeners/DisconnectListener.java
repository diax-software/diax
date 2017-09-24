package me.diax.diax.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DisconnectListener extends ListenerAdapter {

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        close(event.getGuild());
    }

    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event) {
        close(event.getGuild());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        if ((!event.getChannelLeft().getMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser())) && event.getChannelLeft().getMembers().size() < 2) || (!event.getChannelJoined().getMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser())) && event.getChannelJoined().getMembers().size() < 2)) {
            return;
        }
        close(event.getGuild());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if (!event.getChannelLeft().getMembers().contains(event.getGuild().getMember(event.getJDA().getSelfUser())) && event.getChannelLeft().getMembers().size() < 2) {
            close(event.getGuild());
        }
    }

    private void close(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
    }
}