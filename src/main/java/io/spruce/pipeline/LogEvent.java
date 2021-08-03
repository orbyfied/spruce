package io.spruce.pipeline;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;

public class LogEvent {
    Logger   logger;
    String   raw;
    LogLevel level;

    StringBuilder finalTextBuilder;
    boolean       cancelled;

    public LogEvent(Logger logger, String raw, LogLevel level, String currentFinalText) {
        this.logger = logger;
        this.raw    = raw;
        this.level  = level;

        this.finalTextBuilder = new StringBuilder(currentFinalText);
        this.cancelled        = false;
    }

    /*                */
    /* Basic getters. */
    /*                */

    public Logger   getLogger() { return logger; }
    public LogLevel getLevel()  { return level; }
    public String   getRaw()    { return raw; }

    /** Checks if the event has been set cancelled earlier. */
    public boolean isCancelled() {
        return cancelled;
    }

    /** Sets if the event should be cancelled. */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns the StringBuilder for the final used/printed text.
     * @return The StringBuilder object.
     */
    public StringBuilder text() { return finalTextBuilder; }
}
