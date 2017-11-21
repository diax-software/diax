package me.diax.diax.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import ch.qos.logback.core.pattern.color.ANSIConstants;

import static ch.qos.logback.core.pattern.color.ANSIConstants.*;

public class LogbackHighlightConverter extends CompositeConverter<ILoggingEvent> {

    final private static String SET_DEFAULT_COLOR = ESC_START + "0;" + DEFAULT_FG + ESC_END;

    @Override
    protected String transform(ILoggingEvent event, String in) {
        StringBuilder sb = new StringBuilder();
        String color = getForegroundColorCode(event);
        boolean colored = color != null;
        if (colored) {
            sb.append(ESC_START);
            sb.append(getForegroundColorCode(event));
            sb.append(ESC_END);
        }
        sb.append(in);
        if (colored)
            sb.append(SET_DEFAULT_COLOR);
        return sb.toString();
    }

    /**
     * <conversionRule conversionWord="hl" converterClass="com.nachtraben.logback.LogbackHighlightConverter"/>
     */

    protected String getForegroundColorCode(ILoggingEvent event) {
        if (TerminalConsoleAdaptor.isAnsiSupported()) {
            switch (event.getLevel().toInt()) {
                case Level.ERROR_INT:
                    return ANSIConstants.BOLD + ANSIConstants.RED_FG;
                case Level.WARN_INT:
                    return ANSIConstants.BOLD + ANSIConstants.YELLOW_FG;
                case Level.DEBUG_INT:
                    return ANSIConstants.MAGENTA_FG;
                case Level.INFO_INT:
                    return ANSIConstants.CYAN_FG;
                default:
                    return ANSIConstants.DEFAULT_FG;
            }
        }
        return null;
    }
}
