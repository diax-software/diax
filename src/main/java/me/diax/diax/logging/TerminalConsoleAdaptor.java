package me.diax.diax.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.jline.reader.LineReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TerminalConsoleAdaptor extends ConsoleAppender<ILoggingEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalConsoleAdaptor.class);

    public static final String JLINE_OVERRIDE_PROPERTY = "terminal.jline";
    public static final String ANSI_OVERRIDE_PROPERTY = "terminal.ansi";
    public static final Boolean ANSI_OVERRIDE = Boolean.getBoolean(ANSI_OVERRIDE_PROPERTY);
    public static final Boolean JLINE_OVERRIDE = Boolean.getBoolean(JLINE_OVERRIDE_PROPERTY);

    private static TerminalConsoleAdaptor instance;
    private static boolean initialized = false;
    private static Terminal terminal;
    private static LineReader reader;

    public TerminalConsoleAdaptor() {
        if (instance != null)
            throw new IllegalStateException("Terminal appender is already instantiated.");
        instance = this;
    }

    public static void initializeTerminal() {
        if (instance == null)
            throw new IllegalStateException("The appender hasn't been initialized!");

        if (!initialized) {
            initialized = true;
            boolean dumb = JLINE_OVERRIDE || System.getProperty("java.class.path").contains("idea_rt.jar");

            if (!JLINE_OVERRIDE) {
                try {
                    terminal = TerminalBuilder.builder().jansi(isAnsiSupported()).jna(true).dumb(dumb).build();
                    if (isAnsiSupported())
                        instance.setWithJansi(true);
                    instance.setOutputStream(terminal.output());
                    LOGGER.info("Using JLine terminal.");
                } catch (IllegalStateException e) {
                    LOGGER.warn("JLine terminal disabled, running in an unsupported environment.");
                } catch (IOException e) {
                    LOGGER.error("Failed to initialize terminal. Falling back to stdout.", e);
                }
            }
        }
    }

    public static boolean isAnsiSupported() {
        return ANSI_OVERRIDE || terminal != null;
    }

    public static Terminal getTerminal() {
        return terminal;
    }

    public static LineReader getReader() {
        return reader;
    }

    public static void setReader(LineReader reader) {
        if (reader != null && reader.getTerminal() != TerminalConsoleAdaptor.terminal)
            throw new IllegalArgumentException("A reader can only be initialized with TerminalConsoleAdaptor.getTerminal()");
        TerminalConsoleAdaptor.reader = reader;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (terminal != null) {
            if (reader != null) {
                try {
                    reader.callWidget("clear");
                    super.append(event);
                    reader.callWidget("redraw_line");
                    reader.callWidget("redisplay");
                } catch (IllegalStateException e) {
                    super.append(event);
                }
            } else {
                super.append(event);
            }
            terminal.flush();
        } else {
            super.append(event);
        }
    }

    @Override
    public void stop() {
        if (initialized) {
            if (terminal != null) {
                LOGGER.info("Stopping terminal.");
                try {
                    reader = null;
                    terminal.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.stop();
    }
}
