package me.diax.diax.listeners;

import me.diax.diax.util.Emote;
import me.diax.diax.util.JDAUtil;
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
        WebHookUtil.log(event.getJDA(), Emote.SMILEY + " Joined Guild", "```" + event.getGuild().toString().replace("`", "\\`") + "```\n+" + event.getGuild().getMembers().size() + " users!");
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        this.onLeaveOrJoin(event);
        WebHookUtil.log(event.getJDA(), Emote.SOB + " Left Guild", "```" + event.getGuild().toString().replace("`", "\\`") + "```\n-" + event.getGuild().getMembers().size() + " users.");
    }

    private void onLeaveOrJoin(GenericGuildEvent event) {
        try {
            JDAUtil.sendGuilds(event.getJDA(), auth);
        } catch (Exception e) {
            WebHookUtil.log(event.getJDA(), Emote.X + " Error!", "Couldn't update bot list stats.");
        }
    }
}