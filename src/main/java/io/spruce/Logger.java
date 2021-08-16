package io.spruce;

import io.spruce.event.Record;
import io.spruce.pipeline.Pipeline;
import io.spruce.arg.LogLevel;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Logger {

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
    protected Pipeline<Record> pipeline;

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
    public Pipeline<Record> pipeline() { return pipeline; }

    /**
     * Sets the pipeline.
     * @param pipeline The pipeline.
     */
    public void pipeline(Pipeline<Record> pipeline) { this.pipeline = pipeline.cloneFor(this); }

    ////////////////////////////////////////////////////////

    /**
     * Writes a message.
     * @param data The information.
     */
    protected abstract void write0(Record data);

    /**
     * Formats a string into something that
     * can be written using the specified
     * parameter values.
     * @return The formatted string.
     */
    protected abstract String format0(String text, LogLevel level, Object... extra);

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
        String formatted = this.format0(s, l, extra);

        // construct event
        Record event = new Record(this, s, l, formatted);

        // call event and return if cancelled
        boolean accepted = pipeline.event(event);
        if (!accepted) return;

        // retrieve and write message
        this.write0(event);
    }

    public void info   (Object s) { this.log(s, LogLevel.INFO);   }
    public void warn   (Object s) { this.log(s, LogLevel.WARN);   }
    public void severe (Object s) { this.log(s, LogLevel.SEVERE); }

    public void info   (Supplier<?> s) { this.log(s.get(), LogLevel.INFO);   }
    public void warn   (Supplier<?> s) { this.log(s.get(), LogLevel.WARN);   }
    public void severe (Supplier<?> s) { this.log(s.get(), LogLevel.SEVERE); }

    public void info   (Object s, Object... args) { this.log(s, LogLevel.INFO,   args); }
    public void warn   (Object s, Object... args) { this.log(s, LogLevel.WARN,   args); }
    public void severe (Object s, Object... args) { this.log(s, LogLevel.SEVERE, args); }

    public void info   (Supplier<String> s, Object... args) { this.log(s.get(), LogLevel.INFO,   args); }
    public void warn   (Supplier<String> s, Object... args) { this.log(s.get(), LogLevel.WARN,   args); }
    public void severe (Supplier<String> s, Object... args) { this.log(s.get(), LogLevel.SEVERE, args); }

    public void info(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogLevel.INFO, args); }
    public void warn(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogLevel.WARN, args); }
    public void severe(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogLevel.SEVERE, args); }

    /* Basic getters and setters. */

    public String getId()          { return id; }
    public String getTag()         { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
