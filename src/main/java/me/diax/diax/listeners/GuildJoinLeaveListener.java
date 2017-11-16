package me.diax.diax.listeners;

import me.diax.diax.util.JDAUtil;
import me.diax.diax.util.StringUtil;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinLeaveListener extends ListenerAdapter {

    private String auth;

    public GuildJoinLeaveListener(String auth) {
        this.auth = auth;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        this.onLeaveOrJoin(event);
        WebHookUtil.log(event.getJDA(), "Joined Guild:", "```" + StringUtil.stripMarkdown(event.getGuild().toString() + " | +" + event.getGuild().getMembers().size() + " members. | Guilds: " + event.getJDA().getGuilds().size()) + "```");
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        this.onLeaveOrJoin(event);
        WebHookUtil.log(event.getJDA(), "Left Guild:", "```" + StringUtil.stripMarkdown(event.getGuild().toString() + " | -" + event.getGuild().getMembers().size() + " members. | Guilds: " + event.getJDA().getGuilds().size()) + "```");
    }

    private void onLeaveOrJoin(GenericGuildEvent event) {
        JDAUtil.sendGuilds(event.getJDA(), auth);
    }
}