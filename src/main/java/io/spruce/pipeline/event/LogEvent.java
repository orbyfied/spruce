package io.spruce.pipeline.event;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;
import io.spruce.pipeline.Event;

public class LogEvent extends Event {
    Logger   logger;
    String   raw;
    LogLevel level;

    StringBuilder finalTextBuilder;

    public LogEvent(Logger logger, String raw, LogLevel level, String currentFinalText) {
        this.logger = logger;
        this.raw    = raw;
        this.level  = level;

        this.finalTextBuilder = new StringBuilder(currentFinalText);
    }

    /*                */
    /* Basic getters. */
    /*                */

    public Logger   getLogger() { return logger; }
    public LogLevel getLevel()  { return level; }
    public String   getRaw()    { return raw; }

    /**
     * Returns the StringBuilder for the final used/printed text.
     * @return The StringBuilder object.
     */
    public StringBuilder text() { return finalTextBuilder; }
}
