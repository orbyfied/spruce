package io.spruce.pipeline;

import io.spruce.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline<EVENT extends Event> {

    /**
     * A reference to the logger object that this pipeline is for.
     */
    private Logger logger;

    /**
     * A list of log handlers. This is the core of the pipeline.
     */
    private List<Handler<EVENT>> handlers = new ArrayList<>();

    /**
     * A map of all handlers that have a name, by name.
     */
    private Map<String, Handler<EVENT>> handlersByName = new HashMap<>();

    /* Constructors. */
    public Pipeline()                                             { }
    public Pipeline(Logger logger)                                { this.logger = logger; }
    public Pipeline(Logger logger, List<Handler<EVENT>> handlers) { this(logger); this.handlers = handlers; }
    public Pipeline(List<Handler<EVENT>> handlers)                { this.handlers = handlers; }

    /**
     * Clones this pipeline into a new pipeline
     * bound to the specified logger.
     * @param logger The logger to bind it to.
     * @return The created and bound pipeline.
     */
    public Pipeline<EVENT> cloneFor(Logger logger) {
        return new Pipeline<>(logger, new ArrayList<>(this.handlers));
    }

    /**
     * Sends an event through the whole pipeline.
     * @param event The event.
     * @return If the event has NOT been cancelled.
     *         (It is still active.)
     */
    public boolean event(EVENT event) {
        // TODO: code the actual stuff (and more)

        // iterate handlers
        for (Handler<EVENT> h : handlers) h.onEvent(event);

        // return
        return !event.isCancelled();
    }

    /**
     * Clones and returns the cloned list of handlers.
     * @return The list.
     */
    public List<Handler<EVENT>> getHandlers() {
        return new ArrayList<>(handlers);
    }

    public Map<String, Handler<EVENT>> getHandlersByName() { return handlersByName; }

    /**
     * @return The amount of handlers registered.
     */
    public int amount() { return handlers.size(); }

    /** Adds a handler to the tail of the list. */
    public Pipeline<EVENT> addLast(Handler<EVENT> handler) { eCheckNull(handler); handlers.add(handler); return this; }

    /** Adds a handler to the start of the list. */
    public Pipeline<EVENT> addFirst(Handler<EVENT> handler) { eCheckNull(handler); handlers.add(0, handler); return this; }

    /** Inserts a handler at a position in the list. */
    public Pipeline<EVENT> insert(Handler<EVENT> handler, int i) { eCheckNull(handler); handlers.add(i, handler); return this; }

    /** Adds a handler to the tail of the list and maps it to a given name. */
    public Pipeline<EVENT> addLast(String name, Handler<EVENT> handler) {
        this.addLast(handler);
        handlersByName.put(name, handler);
        return this;
    }

    /** Adds a handler to the start of the list and maps it to a given name. */
    public Pipeline<EVENT> addFirst(String name, Handler<EVENT> handler) {
        this.addFirst(handler);
        handlersByName.put(name, handler);
        return this;
    }

    /** Inserts a handler into a given position in the list, and maps it to a given name. */
    public Pipeline<EVENT> insert(String name, Handler<EVENT> handler, int i) {
        this.insert(handler, i);
        handlersByName.put(name, handler);
        return this;
    }

    /** Removes a handler from the list. */
    public Pipeline<EVENT> remove(Handler<EVENT> handler) {
        // remove from list
        handlers.remove(handler);

        // remove from map
        for (Map.Entry<String, Handler<EVENT>> entry : handlersByName.entrySet())
            if (entry.getValue() == handler)
                handlersByName.remove(entry.getKey());

        // return
        return this;
    }

    /** Removes a handler found by the given name. */
    public Pipeline<EVENT> removeByName(String name) {
        // get handler for list removal
        Handler<EVENT> handler = handlersByName.get(name);

        // check null
        if (handler == null) return this;

        // remove handler from list
        handlers.remove(handler);

        // remove handler from map
        handlersByName.remove(name);

        // return
        return this;
    }

    /** Finds a handler by name and returns it. */
    public Handler<EVENT> getByName(String name) { return handlersByName.get(name); }

    /** Finds a handler at the given index and returns it. */
    public Handler<EVENT> getAtIndex(int i) { return (i < handlers.size()) ? handlers.get(i) : null; }

    /** Gets the handler at the start of the pipeline. */
    public Handler<EVENT> getFirst() { return getAtIndex(0); }

    /** Gets the handler at the tail of the pipeline. */
    public Handler<EVENT> getLast() { return getAtIndex(handlers.size() - 1); }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** UTIL: Check for null handler. */
    private void eCheckNull(Handler<EVENT> h) { if(h == null) throw new IllegalArgumentException("handler cannot be null"); }
}
