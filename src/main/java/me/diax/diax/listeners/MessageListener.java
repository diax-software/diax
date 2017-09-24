package me.diax.diax.listeners;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {

    private CommandHandler handler;

    public MessageListener(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        event.getChannel().sendMessage(Emote.X + " - Commands don't currently work in private messages.").queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String prefix;
        if (event.getMessage().getRawContent().startsWith("<>")) {
            prefix = "<>";
        } else if (event.getMessage().getRawContent().startsWith("</>") && event.getAuthor().getId().equals("293884638101897216")) {
            prefix = "</>";
        } else if (event.getMessage().getRawContent().startsWith("<@295500621862404097>")) {
            prefix = "<@295500621862404097>";
        } else {
            return;
        }
        String content = event.getMessage().getRawContent().replaceFirst(Pattern.quote(prefix), "");
        String first = content.split("\\s+")[0].trim();
        try {
            Command command = handler.findCommand(first);
            if (command == null) return;
            if (command.hasAttribute("owner") && !prefix.equals("</>")) return;
            handler.execute(command, event.getMessage(), content);
        } catch (PermissionException ignored) {
        } catch (Exception e) {
            WebHookUtil.log(event.getJDA(), Emote.X + " An exception occurred.", "An uncaught exception occurred when trying to run: ```" + (handler.findCommand(first).getDescription().name() + " | " + event.getGuild() + " | " + event.getChannel()).replace("`", "\\`") + "```");
        }
    }
}