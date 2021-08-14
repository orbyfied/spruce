package io.spruce.event;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;
import io.spruce.pipeline.Event;

import java.util.logging.Formatter;

public class Record extends Event {
    Logger   logger;
    String   raw;
    LogLevel level;

    StringBuilder prefixBuilder;
    StringBuilder finalTextBuilder;
    StringBuilder suffixBuilder;

    public Record(Logger logger, String raw, LogLevel level, String currentFinalText) {
        this.logger = logger;
        this.raw    = raw;
        this.level  = level;

        this.finalTextBuilder = new StringBuilder(currentFinalText);
        this.prefixBuilder    = new StringBuilder();
        this.suffixBuilder    = new StringBuilder();
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

    /**
     * Returns the StringBuilder for the final used/printed prefix.
     * @return The StringBuilder object.
     */
    public StringBuilder prefix() { return prefixBuilder; }

    /**
     * Returns the StringBuilder for the final used/printed suffix.
     * @return The StringBuilder object.
     */
    public StringBuilder suffix() { return suffixBuilder; }
}