package me.diax.diax.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.PatternLayout
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy
import net.dv8tion.jda.core.entities.MessageChannel
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class DiscordLogBack : AppenderBase<ILoggingEvent>() {
    private var layout: PatternLayout? = null

    @Synchronized private fun boot(channel: MessageChannel) {
        if (thread != null) {
            throw IllegalStateException("Processor Thread already running")
        }

        thread = object : Thread("DiscordLogBack Processor") {
            override fun run() {
                var event: ILoggingEvent?

                while (true) {
                    event = queue.take()

                    val joiner = StringJoiner("\n")
                    joiner.add(layout!!.doLayout(event))

                    val start = System.currentTimeMillis()
                    while (true) {
                        event = queue.poll(500, TimeUnit.MILLISECONDS)

                        if (event == null) break

                        joiner.add(layout!!.doLayout(event))

                        if (System.currentTimeMillis() > start + 1000) break
                    }

                    for (message in MessageBuilder().append(joiner.toString()).buildAll(SplitPolicy.SPACE)) {
                        channel.sendMessage(message).queue()
                    }
                }
            }
        }
        thread!!.start()
    }

    override fun append(event: ILoggingEvent) {
        if (event.level.isGreaterOrEqual(Level.INFO)) {
            synchronized(queue) {
                queue.offer(event)
            }
        }
    }

    override fun start() {
        if (instance != null && instance != this) {
            return
        }

        instance = this

        layout = PatternLayout()

        //Configuration
        layout!!.context = getContext()
        layout!!.pattern = "[`%d{HH:mm:ss}`] [`%t/%level`] [`%logger{0}`]: %msg"
        layout!!.start()

        super.start()
    }

    companion object {
        private val queue = LinkedBlockingQueue<ILoggingEvent>()
        private var instance: DiscordLogBack? = null
        private var thread: Thread? = null

        fun disable() {
            if (thread != null) {
                thread!!.interrupt()
            }

            queue.clear()
        }

        fun enable(channel: MessageChannel) {
            if (instance == null) {
                //This trace call itself should be enough to boot up Logback
                LoggerFactory.getLogger(DiscordLogBack::class.java).trace("Logback wasn't initialized; Attempting to boot it...")
            }

            if (instance == null) {
                throw IllegalStateException("DiscordLogBack instance not initialized.")
            }

            instance!!.boot(channel)
        }
    }
}