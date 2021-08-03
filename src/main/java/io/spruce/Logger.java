package io.spruce;

import io.spruce.pipeline.event.LogEvent;
import io.spruce.pipeline.LoggerPipeline;
import io.spruce.arg.LogLevel;

public abstract class Logger {
    /**
     * A tag/ID that the logger can be recognized by.
     */
    protected String tag;

    /**
     * A pipeline of handlers. Used to process events.
     */
    protected LoggerPipeline<LogEvent> pipeline;

    /** Basic constructor. */
    public Logger(String tag) {
        this.tag      = tag;
        this.pipeline = new LoggerPipeline<>(this);
    }

    /**
     * Gets the logger's pipeline.
     * @return The LoggerPipeline.
     */
    public LoggerPipeline<LogEvent> pipeline() { return pipeline; }

    /**
     * Sets the pipeline.
     * @param pipeline The pipeline.
     */
    public void pipeline(LoggerPipeline<LogEvent> pipeline) { this.pipeline = pipeline.cloneFor(this); }

    ////////////////////////////////////////////////////////

    /**
     * Writes a message.
     * @param str The message.
     */
    protected abstract void write(LogEvent data);

    /**
     * Formats a string into something that
     * can be written using the specified
     * parameter values.
     * @return The formatted string.
     */
    protected abstract String formatPrimary(String text, LogLevel level, Object... extra);

    /**
     * Formats a string using the <code>formatPrimary(...)</code> method
     * and writes/logs it using the <code>write(...)</code> method.
     * @param s The message.
     * @param l The logging level.
     * @param extra Extra parameters that the
     *              logger may accept.
     */
    public void log(String s, LogLevel l, Object... extra) {
        // format string
        String formatted = this.formatPrimary(s, l, extra);

        // construct event
        LogEvent event = new LogEvent(this, s, l, formatted);

        // call event and return if cancelled
        boolean accepted = pipeline.event(event);
        if (!accepted) return;

        // retrieve and write message
        this.write(event);
    }

    public void info   (String s) { this.log(s, LogLevel.INFO); }
    public void warn   (String s) { this.log(s, LogLevel.WARN); }
    public void severe (String s) { this.log(s, LogLevel.SEVERE); }

    /* Basic getters and setters. */

    public String getTag() { return tag; }
}
