package io.spruce.pipeline;

import io.spruce.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LoggerPipeline {

    /**
     * A reference to the logger object that this pipeline is for.
     */
    private Logger logger;

    /**
     * A list of log handlers. This is the core of the pipeline.
     */
    private List<LogHandler> handlers = new ArrayList<>();

    /* Constructors. */
    public LoggerPipeline(Logger logger)                            { this.logger = logger; }
    public LoggerPipeline(Logger logger, List<LogHandler> handlers) { this(logger); this.handlers = handlers; }
    public LoggerPipeline(List<LogHandler> handlers)                { this.handlers = handlers; }

    /**
     * Clones this pipeline into a new pipeline
     * bound to the specified logger.
     * @param logger The logger to bind it to.
     * @return The created and bound pipeline.
     */
    public LoggerPipeline cloneFor(Logger logger) {
        return new LoggerPipeline(logger, new ArrayList<>(this.handlers));
    }

    /**
     * Sends an event through the whole pipeline.
     * @param event The event.
     * @return If the event has NOT been cancelled.
     *         (It is still active.)
     */
    public boolean event(LogEvent event) {
        // TODO: code the actual stuff (and more)

        // iterate handlers
        for (LogHandler h : handlers) h.onLog(event);

        // return
        return !event.isCancelled();
    }

    /**
     * Clones and returns the cloned list of handlers.
     * @return The list.
     */
    public List<LogHandler> getHandlers() {
        return new ArrayList<>(handlers);
    }

    /**
     * @return The amount of handlers registered.
     */
    public int amount() { return handlers.size(); }

    /** Adds a handler to the tail of the list. */
    public LoggerPipeline addLast(LogHandler handler) { eCheckNull(handler); handlers.add(handler); return this; }

    /** Adds a handler to the start of the list. */
    public LoggerPipeline addFirst(LogHandler handler) { eCheckNull(handler); handlers.add(0, handler); return this; }

    /** Inserts a handler at a position in the list. */
    public LoggerPipeline insert(LogHandler handler, int i) { eCheckNull(handler); handlers.add(i, handler); return this; }

    /** Removes a handler from the list. */
    public LoggerPipeline remove(LogHandler handler) { handlers.remove(handler); return this; }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private void eCheckNull(LogHandler h) { if(h == null) throw new IllegalArgumentException("handler cannot be null"); }
}
