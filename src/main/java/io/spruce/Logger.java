package io.spruce;

import io.spruce.event.LogEvent;
import io.spruce.pipeline.Pipeline;
import io.spruce.arg.LogLevel;

public abstract class Logger {

    // this is required to load spruces
    static int _INIT;
    static { _INIT = 1; Spruce._initializer.isInitialized(); }

    /**
     * A tag/ID that the logger can be recognized by.
     */
    protected String id;

    /**
     * A string that can be used as a sort of
     * display name for this logger.
     */
    protected String tag;

    /**
     * A pipeline of handlers. Used to process events.
     */
    protected Pipeline<LogEvent> pipeline;

    /** Basic constructor. */
    public Logger(String id) {
        this.id       = id;
        this.pipeline = new Pipeline<>(this);
    }

    /** Even more basic constructor. */
    public Logger() {
        this.id       = "";
        this.pipeline = new Pipeline<>(this);
    }

    /**
     * Gets the logger's pipeline.
     * @return The LoggerPipeline.
     */
    public Pipeline<LogEvent> pipeline() { return pipeline; }

    /**
     * Sets the pipeline.
     * @param pipeline The pipeline.
     */
    public void pipeline(Pipeline<LogEvent> pipeline) { this.pipeline = pipeline.cloneFor(this); }

    ////////////////////////////////////////////////////////

    /**
     * Writes a message.
     * @param data The information.
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
     * @param o The message.
     * @param l The logging level.
     * @param extra Extra parameters that the
     *              logger may accept.
     */
    public void log(Object o, LogLevel l, Object... extra) {
        // tostring object
        String s = (o != null) ? o.toString() : "null";

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

    public void info   (Object s) { this.log(s, LogLevel.INFO); }
    public void warn   (Object s) { this.log(s, LogLevel.WARN); }
    public void severe (Object s) { this.log(s, LogLevel.SEVERE); }

    /* Basic getters and setters. */

    public String getId()          { return id; }
    public String getTag()         { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
