package me.diax.diax.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.SneakyThrows;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.MessageBuilder.SplitPolicy;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DiscordLogBack extends AppenderBase<ILoggingEvent> {
    private static final BlockingQueue<ILoggingEvent> queue = new LinkedBlockingQueue<>();
    private static DiscordLogBack instance;
    private static Thread thread;
    private PatternLayout layout;

    public static void disable() {
        if (thread != null) {
            thread.interrupt();
        }

        queue.clear();
    }

    public static void enable(MessageChannel channel) {
        if (instance == null) {
            //This trace call itself should be enough to boot up Logback
            LoggerFactory.getLogger(DiscordLogBack.class).trace("Logback wasn't initialized; Attempting to boot it...");
        }

        if (instance == null) {
            throw new IllegalStateException("DiscordLogBack instance not initialized.");
        }

        instance.boot(channel);
    }

    private synchronized void boot(MessageChannel channel) {
        if (thread != null) {
            throw new IllegalStateException("Processor Thread already running");
        }

        thread = new Thread("DiscordLogBack Processor") {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            @SneakyThrows
            public void run() {
                ILoggingEvent event;

                while (true) {
                    event = queue.take();

                    StringJoiner joiner = new StringJoiner("\n");
                    joiner.add(layout.doLayout(event));

                    long start = System.currentTimeMillis();
                    while (true) {
                        event = queue.poll(500, TimeUnit.MILLISECONDS);

                        if (event == null) break;

                        joiner.add(layout.doLayout(event));

                        if (System.currentTimeMillis() > start + 1000) break;
                    }

                    for (Message message : new MessageBuilder().append(joiner.toString()).buildAll(SplitPolicy.SPACE)) {
                        channel.sendMessage(message).queue();
                    }
                }
            }
        };

        thread.start();
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (event.getLevel().isGreaterOrEqual(Level.INFO)) {
            synchronized (queue) {
                queue.offer(event);
            }
        }
    }

    @Override
    public void start() {
        if (instance != null && instance != this) {
            return;
        }

        instance = this;

        layout = new PatternLayout();

        //Configuration
        layout.setContext(getContext());
        layout.setPattern("[`%d{HH:mm:ss}`] [`%t/%level`] [`%logger{0}`]: %msg");
        layout.start();

        super.start();
    }
}