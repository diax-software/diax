package me.diax.diax.listeners

import br.com.brjdevs.java.utils.texts.StringUtils
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.commands.CommandPermission
import me.diax.diax.data.ManagedDatabase
import me.diax.diax.data.config.entities.Config
import me.diax.diax.shards.DiaxShard
import me.diax.diax.util.Emote
import me.diax.diax.util.Emote.STOP
import me.diax.diax.util.WebHookUtil
import mu.KLogging
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class CommandListener(
    val shard: DiaxShard,
    val db: ManagedDatabase,
    val handler: CommandHandler,
    val config: Config
) : ListenerAdapter() {

    companion object : KLogging()

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot) return
        if (event.message.isWebhookMessage) return
        if (config.blacklist.contains(event.author.id)) return
        if (event.guild != null && !event.textChannel.canTalk()) return

        shard.commandPool.submit { onCommand(event) }
    }

    private fun onCommand(event: MessageReceivedEvent) {
        val raw = event.message.rawContent

        for (prefix in config.prefixes) {
            if (raw.startsWith(prefix)) {
                process(event, raw.substring(prefix.length))
                return
            }
        }

        val mentions = listOf("<@${event.author.id}> ", "<@!${event.author.id}> ")
        for (mention in mentions) {
            if (raw.startsWith(mention)) {
                process(event, raw.substring(mention.length))
                return
            }
        }

        val guildPrefix = db[event.guild].settings.prefix
        if (guildPrefix != null && raw.startsWith(guildPrefix)) {
            process(event, raw.substring(guildPrefix.length))
            return
        }
    }

    private fun process(event: MessageReceivedEvent, content: String) {
        val split = StringUtils.splitArgs(content, 2)
        val cmd = split[0]!!
        val args = split[1]!!

        val command = handler.findCommand(cmd) ?: return processCustomCommand(event, cmd, args)

        if (command.hasAttribute("permission")) {
            val permission = CommandPermission.valueOf(command.getAttributeValueFromKey("permission").toUpperCase())

            if (!if (event.member == null) permission.test(this, event.author) else permission.test(this, event.author)) {
                event.channel.sendMessage("${STOP} You have no permissions to trigger this command :(").queue()
                return
            }
        }

        if (command.hasAttribute("channel")) {
            val channelType = ChannelType.valueOf(command.getAttributeValueFromKey("channel").toUpperCase())

            if (event.channel.type != channelType) {
                event.channel.sendMessage("${Emote.X} This command only works in ${channelType.name.toLowerCase()} channels.").queue()
                //Error?
                return
            }
        }

        if (command.hasAttribute("patreon") && !(config.donors.contains(event.author.id) || config.developers.contains(event.author.id))) {
            event.channel.sendMessage("${Emote.X} This is a Patreon-only command.").queue()
            return
        }

        runCommand(command, event, args)
    }

    private fun processCustomCommand(event: MessageReceivedEvent, cmd: String, args: String) {

    }

    private fun runCommand(command: Command, event: MessageReceivedEvent, args: String) {
        try {
            command.execute(event.message, args)
        } catch (e: Exception) {
            try {
                event.channel.sendMessage(Emote.X + " - Something went wrong that we didn't know about ;-;\nJoin here for help: https://discord.gg/PedN8U").queue()
            } catch (_: Exception) {
            }

            e.printStackTrace()
            WebHookUtil.log(event.jda, Emote.X + " An exception occurred.", "An uncaught exception occurred when trying to run: ```" + (command.description.name + " | " + event.guild + " | " + event.channel).replace("`", "\\`") + "```")
        }

    }
}