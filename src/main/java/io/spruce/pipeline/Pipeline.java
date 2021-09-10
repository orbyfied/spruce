package io.spruce.pipeline;

import io.spruce.Logger;
import io.spruce.meta.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline<FLUID extends Fluid> {

    /**
     * A reference to the logger object that this pipeline is for.
     */
    private PipelineHolder<FLUID> holder;

    /**
     * A list of log handlers. This is the core of the pipeline.
     */
    private List<Part<FLUID>> handlers = new ArrayList<>();

    /**
     * A map of all handlers that have a name, by name.
     */
    private Map<String, Part<FLUID>> handlersByName = new HashMap<>();

    /* Constructors. */
    public Pipeline() { }
    public Pipeline(PipelineHolder<FLUID> holder) { this.holder = holder; }
    public Pipeline(PipelineHolder<FLUID> holder, List<Part<FLUID>> handlers) { this(holder); this.handlers = handlers; }
    public Pipeline(List<Part<FLUID>> handlers) { this.handlers = handlers; }

    /**
     * Clones this pipeline into a new pipeline
     * bound to the specified logger.
     * @param holder The holder to bind it to.
     * @return The created and bound pipeline.
     */
    public Pipeline<FLUID> cloneFor(PipelineHolder<FLUID> holder) {
        return new Pipeline<>(holder, new ArrayList<>(this.handlers));
    }

    /**
     * Returns the holder that this pipeline is bound to.
     * Just returns null if it is unbound.
     * @return The holder. (or null)
     */
    @Nullable
    public PipelineHolder<FLUID> getHolder() {
        return holder;
    }

    /**
     * Checks if the pipeline is bound to a holder.
     * @return Boolean.
     */
    public boolean isBound() {
        return holder != null;
    }

    /**
     * Sends an event through the whole pipeline.
     * @param event The event.
     * @return If the event was successfully passed through the pipeline.
     */
    public boolean in(FLUID event) {
        try {
            // check if the call is accepted
            if (isBound())
                if (!holder.eventPassed(event))
                    return false;

            // iterate handlers
            for (Part<FLUID> h : handlers) h.reach(this, event);

            // return
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Clones and returns the cloned list of handlers.
     * @return The list.
     */
    public List<Part<FLUID>> getHandlers() {
        return new ArrayList<>(handlers);
    }

    public Map<String, Part<FLUID>> getHandlersByName() { return handlersByName; }

    /**
     * @return The amount of handlers registered.
     */
    public int amount() { return handlers.size(); }

    /** Calls 'part.added(this)'. */
    private boolean callAdded(Part<FLUID> part) { return part.added(this); }

    /** Adds a handler to the tail of the list. */
    public Pipeline<FLUID> addLast(Part<FLUID> handler) {
        eCheckNull(handler);
        if (callAdded(handler))
            handlers.add(handler);
        return this;
    }

    /** Adds a handler to the start of the list. */
    public Pipeline<FLUID> addFirst(Part<FLUID> handler) {
        eCheckNull(handler);
        if (callAdded(handler))
            handlers.add(0, handler);
        return this;
    }

    /** Inserts a handler at a position in the list. */
    public Pipeline<FLUID> insert(Part<FLUID> handler, int i) {
        eCheckNull(handler);
        if (callAdded(handler))
            handlers.add(i, handler);
        return this;
    }

    /** Adds a handler to the tail of the list and maps it to a given name. */
    public Pipeline<FLUID> addLast(String name, Part<FLUID> handler) {
        eCheckNull(handler);
        if (callAdded(handler)) {
            handlers.add(handler);
            handlersByName.put(name, handler);
        }
        return this;
    }

    /** Adds a handler to the start of the list and maps it to a given name. */
    public Pipeline<FLUID> addFirst(String name, Part<FLUID> handler) {
        eCheckNull(handler);
        if (callAdded(handler)) {
            handlers.add(0, handler);
            handlersByName.put(name, handler);
        }
        return this;
    }

    /** Inserts a handler into a given position in the list, and maps it to a given name. */
    public Pipeline<FLUID> insert(String name, Part<FLUID> handler, int i) {
        eCheckNull(handler);
        if (callAdded(handler)) {
            handlers.add(i, handler);
            handlersByName.put(name, handler);
        }
        return this;
    }

    /** Removes a handler from the list. */
    public Pipeline<FLUID> remove(Part<FLUID> handler) {
        // remove from list
        handlers.remove(handler);

        // remove from map
        for (Map.Entry<String, Part<FLUID>> entry : handlersByName.entrySet())
            if (entry.getValue() == handler)
                handlersByName.remove(entry.getKey());

        // call removed
        handler.removed(this);

        // return
        return this;
    }

    /** Removes a handler found by the given name. */
    public Pipeline<FLUID> removeByName(String name) {
        // get handler for list removal
        Part<FLUID> handler = handlersByName.get(name);

        // check null
        if (handler == null) return this;

        // remove handler from list
        handlers.remove(handler);

        // remove handler from map
        handlersByName.remove(name);

        // call removed
        handler.removed(this);

        // return
        return this;
    }

    /** Finds a handler by name and returns it. */
    public Part<FLUID> getByName(String name) { return handlersByName.get(name); }

    /** Finds a handler at the given index and returns it. */
    public Part<FLUID> getAtIndex(int i) { return (i < handlers.size()) ? handlers.get(i) : null; }

    /** Gets the handler at the start of the pipeline. */
    public Part<FLUID> getFirst() { return getAtIndex(0); }

    /** Gets the handler at the tail of the pipeline. */
    public Part<FLUID> getLast() { return getAtIndex(handlers.size() - 1); }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /** UTIL: Check for null handler. */
    private void eCheckNull(Part<FLUID> h) { if(h == null) throw new IllegalArgumentException("handler cannot be null"); }
}
