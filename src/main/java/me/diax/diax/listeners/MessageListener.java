package me.diax.diax.listeners;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.util.Data;
import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {

    private CommandHandler handler;
    private Data data;

    public MessageListener(CommandHandler handler, Data data) {
        this.handler = handler;
        this.data = data;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage() || data.getBlacklist().contains(event.getAuthor().getId()))
            return;
        String prefix;
        if (event.getMessage().getRawContent().startsWith(data.getPrefix())) {
            prefix = data.getPrefix();
        } else if (event.getMessage().getRawContent().startsWith(event.getJDA().getSelfUser().getAsMention())) {
            prefix = event.getJDA().getSelfUser().getAsMention();
        } else if (event.getChannelType().equals(ChannelType.PRIVATE)) {
            prefix = "";
        } else {
            return;
        }
        String content = event.getMessage().getRawContent().replaceFirst(Pattern.quote(prefix), "").trim();
        String first = content.split("\\s+")[0].trim();
        try {
            Command command = handler.findCommand(first);
            if (command == null) return;
            if (command.hasAttribute("patreon") && !(data.getDonors().contains(event.getAuthor().getId()) || data.getDevelopers().contains(event.getAuthor().getId()))) {
                event.getChannel().sendMessage(Emote.X + " - This is a Patreon-only command.").queue();
                return;
            }
            if (command.hasAttribute("developer") && (!data.getDevelopers().contains(event.getAuthor().getId())))
                return;
            if (event.getChannelType().equals(ChannelType.PRIVATE) && command.hasAttribute("private")) {
                event.getChannel().sendMessage(Emote.X + " - This command does not work in private messages.").queue();
                return;
            }
            handler.execute(command, event.getMessage(), content.replaceFirst(Pattern.quote(first), ""));
        } catch (PermissionException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            WebHookUtil.log(event.getJDA(), Emote.X + " An exception occurred.", "An uncaught exception occurred when trying to run: ```" + (handler.findCommand(first).getDescription().name() + " | " + event.getGuild() + " | " + event.getChannel()).replace("`", "\\`") + "```");
        }
    }
}