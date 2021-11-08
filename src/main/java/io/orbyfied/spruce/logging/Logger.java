package io.orbyfied.spruce.logging;

import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.io.OutputWorker;
import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.pipeline.PipelineHolder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Logger implements PipelineHolder<Record> {

    protected LoggerWorker worker;

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
     *
     */
    protected Pipeline<Record> prePipeline;

    /**
     * A pipeline of handlers. Used to process events.
     */
    protected Pipeline<Record> pipeline;

    /** Basic constructor. */
    public Logger(String id) {
        this.id       = id;
        this.worker   = new LoggerWorker(this);

        this.pipeline    = new Pipeline<>(this);
        this.prePipeline = new Pipeline<>(this);
    }

    /** Even more basic constructor. */
    public Logger() {
        this("");
    }

    /**
     * Gets the loggers pipeline.
     * @return The pipeline.
     */
    public Pipeline<Record> pipeline() { return pipeline; }

    /**
     * Sets the pipeline.
     * @param pipeline The pipeline.
     */
    public void pipeline(Pipeline<Record> pipeline) { this.pipeline = pipeline.cloneFor(this); }

    /**
     * Gets the loggers pre-pipeline.
     * @return The LoggerPipeline.
     */
    public Pipeline<Record> prePipeline() { return prePipeline; }

    /**
     * Sets the pre-pipeline.
     * @param pipeline The pipeline.
     */
    public void prePipeline(Pipeline<Record> pipeline) { this.prePipeline = prePipeline.cloneFor(this); }

    ////////////////////////////////////////////////////////

    /**
     * Is supposed to post-process the event and
     * return all output workers the event should be queued to.
     * @param record The event.
     * @return The list of output workers.
     */
    protected List<OutputWorker> write0(Record record) {
        return Logging.INITIAL_OUTPUTS;
    }

    /**
     * Formats a string into something that
     * can be written using the specified
     * parameter values.
     * @return The formatted string.
     */
    protected abstract String format0(String text, LogType level, Object... extra);

    /**
     * Formats a string using the <code>formatPrimary(...)</code> method
     * and writes/logs it using the <code>write(...)</code> method.
     * @param o The message.
     * @param l The logging level.
     * @param extra Extra parameters that the
     *              logger may accept.
     */
    public void log(Object o, LogType l, Object... extra) {
        // tostring object
        String s = (o != null) ? o.toString() : "null";

        // construct event
        Record event = new Record(this, s, l, s, extra);

        // call event and return if cancelled
        prePipeline.in(event);
        if (event.isCancelled()) return;

        // schedule
        worker.queue(event);
    }

    public void info   (Object s) { this.log(s, LogType.INFO);   }
    public void warn   (Object s) { this.log(s, LogType.WARN);   }
    public void severe (Object s) { this.log(s, LogType.SEVERE); }
    public void debug  (Object s) { this.log(s, LogType.DEBUG);  }

    public void info   (Supplier<?> s) { this.log(s.get(), LogType.INFO);   }
    public void warn   (Supplier<?> s) { this.log(s.get(), LogType.WARN);   }
    public void severe (Supplier<?> s) { this.log(s.get(), LogType.SEVERE); }
    public void debug  (Supplier<?> s) { this.log(s.get(), LogType.DEBUG);  }

    public void info   (Object s, Object... args) { this.log(s, LogType.INFO,   args); }
    public void warn   (Object s, Object... args) { this.log(s, LogType.WARN,   args); }
    public void severe (Object s, Object... args) { this.log(s, LogType.SEVERE, args); }
    public void debug  (Object s, Object... args) { this.log(s, LogType.DEBUG,  args); }

    public void info   (Supplier<?> s, Object... args) { this.log(s.get(), LogType.INFO,   args); }
    public void warn   (Supplier<?> s, Object... args) { this.log(s.get(), LogType.WARN,   args); }
    public void severe (Supplier<?> s, Object... args) { this.log(s.get(), LogType.SEVERE, args); }
    public void debug  (Supplier<?> s, Object... args) { this.log(s.get(), LogType.DEBUG,  args); }

    public void info(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogType.INFO, args); }
    public void warn(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogType.WARN, args); }
    public void severe(Function<Logger, String> f, Object... args) {
        this.log(f.apply(this), LogType.SEVERE, args); }

    /* Basic getters and setters. */

    public String getId()          { return id; }
    public String getTag()         { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
