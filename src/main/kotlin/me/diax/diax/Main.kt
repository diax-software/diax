package me.diax.diax

import com.google.inject.Injector
import me.diax.comportment.jdacommand.Command
import me.diax.comportment.jdacommand.CommandDescription
import me.diax.comportment.jdacommand.CommandHandler
import me.diax.diax.data.ManagedDatabase
import me.diax.diax.data.config.ConfigManager
import me.diax.diax.injection.DiaxInjections
import me.diax.diax.listeners.CommandListener
import me.diax.diax.listeners.GuildJoinLeaveListener
import me.diax.diax.logging.TerminalConsoleAdaptor
import me.diax.diax.shards.DiaxShard
import me.diax.diax.util.DiscordLogBack
import me.diax.diax.util.Emote
import me.diax.diax.util.JDAUtil
import me.diax.diax.util.WebHookUtil
import me.diax.diax.util.style.BotType
import mu.KLogging
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import org.reflections.Reflections
import java.lang.reflect.Modifier

private object Main : KLogging()

val log = Main.logger

fun main(args: Array<String>) {
    TerminalConsoleAdaptor.initializeTerminal()

    log.info("Starting Diax...")

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

    //Welcome to automation
    handler.registerCommands(
        reflections.getSubTypesOf(Command::class.java)
            .filter { !Modifier.isAbstract(it.modifiers) && it.isAnnotationPresent(CommandDescription::class.java) }
            .map { injector[it] }
            .toSet()
    )

    val jda = JDABuilder(AccountType.BOT)
        .setToken(manager.get().tokens.discord!!)
        .setAudioEnabled(true)
        .setGame(Game.playing("Diax is starting, hold tight!"))
        .setStatus(OnlineStatus.IDLE)
        .addEventListener(
            GuildJoinLeaveListener(manager.get().tokens.botlist),
            CommandListener(
                DiaxShard(0, 0),
                injector[ManagedDatabase::class.java],
                handler,
                manager.get()
            )
        ).buildBlocking()

    DiscordLogBack.enable(jda.getTextChannelById(manager.get().channels.output))

    WebHookUtil.log(jda, Emote.SPARKLES + " Start", "Diax has finished starting!")

    JDAUtil.startGameChanging(jda, manager.get().prefixes[0])
    JDAUtil.sendGuilds(jda, manager.get().tokens.botlist)
}

operator fun <T> Injector.get(clazz: Class<T>): T {
    return getInstance(clazz)
}
