package me.diax.diax

import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.injection.DiaxInjections
import me.diax.diax.listeners.GuildJoinLeaveListener
import me.diax.diax.listeners.MessageListener
import me.diax.diax.util.DiscordLogBack
import me.diax.diax.util.Emote
import me.diax.diax.util.JDAUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.style.BotType
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.reflections.Reflections
import java.lang.reflect.Modifier

object Main {
    private val log = org.slf4j.LoggerFactory.getLogger(Main.javaClass)

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val manager = ConfigManager()
        Runtime.getRuntime().addShutdownHook(Thread(Runnable { manager.save() }))

        try {
            manager.load()
        } catch (e: Exception) {
            manager.save()
            log.error("Couldn't load data file. Please load it again")
            e.printStackTrace()
            System.exit(1)
        }

        manager.save()

        BotType.CURRENT_TYPE = BotType.valueOf(manager.get().type!!.toUpperCase())

        //todo maybe move code to a separated class

        val reflections = Reflections("me.diax.diax")
        val handler = CommandHandler()

        val injector = DiaxInjections(handler, manager).toInjector()


        val commands: Set<Command> = reflections.getSubTypesOf(Command::class.java)
            .filter { c -> !Modifier.isAbstract(c.modifiers) && c.isAnnotationPresent(CommandDescription::class.java) }
            .map { injector.getInstance(it) }
            .toSet()

        //Welcome to automation
        handler.registerCommands(commands)

        JDABuilder(AccountType.BOT)
            .setToken(manager.get().tokens.discord)
            .setAudioEnabled(true)
            .setGame(Game.of("Diax is starting, hold tight!"))
            .setStatus(OnlineStatus.IDLE)
            .addEventListener(
                GuildJoinLeaveListener(manager.get().tokens.botlist!!),
                MessageListener(handler, manager.get()),
                object : ListenerAdapter() {
                    override fun onReady(event: ReadyEvent?) {
                        DiscordLogBack.enable(event!!.jda.getTextChannelById(manager.get().channels.output))
                        val jda = event.jda
                        WebHookUtil.log(jda, Emote.SPARKLES + " Start", "Diax has finished starting!")
                        JDAUtil.startGameChanging(jda, manager.get().prefix!!)
                        JDAUtil.sendGuilds(event.jda, manager.get().tokens.botlist)
                    }
                }
            ).buildBlocking()
    }
}