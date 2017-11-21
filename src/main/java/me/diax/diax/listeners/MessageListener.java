package me.diax.diax.listeners;

import me.diax.comportment.jdacommand.Command;
import me.diax.comportment.jdacommand.CommandHandler;
import me.diax.diax.data.config.entities.Config;
import me.diax.diax.util.Emote;
import me.diax.diax.util.WebHookUtil;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {

    private CommandHandler handler;
    private Config config;

    public MessageListener(CommandHandler handler, Config config) {
        this.handler = handler;
        this.config = config;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage() || config.getBlacklist().contains(event.getAuthor().getId()))
            return;
        String prefix;
        if (event.getMessage().getRawContent().startsWith(config.getPrefix())) {
            prefix = config.getPrefix();
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
            if (command.hasAttribute("developer") && (!config.getDevelopers().contains(event.getAuthor().getId())))
                return;
            if (command.hasAttribute("fun") || command.hasAttribute("image") || command.hasAttribute("information")) {
            }
            if ((command.hasAttribute("action") || command.hasAttribute("music")) && !event.getChannelType().equals(ChannelType.TEXT)) {
                return; // ERROR: ONLY GUILD
            }
            if (command.hasAttribute("patreon") && !(config.getDonors().contains(event.getAuthor().getId()) || config.getDevelopers().contains(event.getAuthor().getId()))) {
                event.getChannel().sendMessage(Emote.X + " - This is a Patreon-only command.").queue();
                return;
            }
            if (event.getChannelType().equals(ChannelType.PRIVATE) && command.hasAttribute("private")) {
                event.getChannel().sendMessage(Emote.X + " - This command does not work in private messages.").queue();
                return;
            }
            handler.execute(command, event.getMessage(), content.replaceFirst(Pattern.quote(first), ""));
        } catch (PermissionException ignored) {
        } catch (Exception e) {
            try {
                event.getChannel().sendMessage(Emote.X + " - Something went wrong that we didn't know about ;-;\nJoin here for help: https://discord.gg/PedN8U").queue();
            } catch (Exception ignored) {
            }
            e.printStackTrace();
            WebHookUtil.log(event.getJDA(), Emote.X + " An exception occurred.", "An uncaught exception occurred when trying to run: ```" + (handler.findCommand(first).getDescription().name() + " | " + event.getGuild() + " | " + event.getChannel()).replace("`", "\\`") + "```");
        }
    }
}