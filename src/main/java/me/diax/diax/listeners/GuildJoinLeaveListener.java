package me.diax.diax.listeners;

import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        WebHookUtil.log(event.getJDA(), Emote.SMILEY + " Joined Guild", "```" + event.getGuild().toString().replace("`", "\\`") + "```\n+" + event.getGuild().getMembers().size() + " users!");
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        WebHookUtil.log(event.getJDA(), Emote.SOB + " Left Guild", "```" + event.getGuild().toString().replace("`", "\\`") + "```\n-" + event.getGuild().getMembers().size() + " users.");
    }
}