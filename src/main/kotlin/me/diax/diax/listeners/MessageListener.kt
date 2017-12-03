package me.diax.diax.listeners

import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.data.config.entities.Config
import me.diax.diax.util.Emote
import me.diax.diax.util.WebHookUtil
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.regex.Pattern

class MessageListener(private val handler: CommandHandler, private val config: Config) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot || event.message.isWebhookMessage || config.blacklist.contains(event.author.id))
            return
        val prefix: String?
        if (event.message.rawContent.startsWith(config.prefix!!)) {
            prefix = config.prefix
        } else if (event.message.rawContent.startsWith(event.jda.selfUser.asMention)) {
            prefix = event.jda.selfUser.asMention
        } else if (event.channelType == ChannelType.PRIVATE) {
            prefix = ""
        } else {
            return
        }
        val content = event.message.rawContent.replaceFirst(Pattern.quote(prefix!!).toRegex(), "").trim { it <= ' ' }
        val first = content.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
        try {
            val command = handler.findCommand(first) ?: return
            if (command.hasAttribute("developer") && !config.developers.contains(event.author.id))
                return
            if (command.hasAttribute("fun") || command.hasAttribute("image") || command.hasAttribute("information")) {
            }
            if ((command.hasAttribute("action") || command.hasAttribute("music")) && event.channelType != ChannelType.TEXT) {
                return  // ERROR: ONLY GUILD
            }
            if (command.hasAttribute("patreon") && !(config.donors.contains(event.author.id) || config.developers.contains(event.author.id))) {
                event.channel.sendMessage(Emote.X + " - This is a Patreon-only command.").queue()
                return
            }
            if (event.channelType == ChannelType.PRIVATE && command.hasAttribute("private")) {
                event.channel.sendMessage(Emote.X + " - This command does not work in private messages.").queue()
                return
            }
            handler.execute(command, event.message, content.replaceFirst(Pattern.quote(first).toRegex(), ""))
        } catch (ignored: PermissionException) {
        } catch (e: Exception) {
            try {
                event.channel.sendMessage(Emote.X + " - Something went wrong that we didn't know about ;-;\nJoin here for help: https://discord.gg/PedN8U").queue()
            } catch (ignored: Exception) {
            }

            e.printStackTrace()
            WebHookUtil.log(event.jda, Emote.X + " An exception occurred.", "An uncaught exception occurred when trying to run: ```" + (handler.findCommand(first).description.name + " | " + event.guild + " | " + event.channel).replace("`", "\\`") + "```")
        }

    }
}