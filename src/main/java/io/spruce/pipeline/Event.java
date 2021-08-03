package io.spruce.pipeline;

public class Event {
    /**
     * Has the event been cancelled?
     */
    protected boolean cancelled = false;

    /**
     * Sets the cancel status.
     * @param cancelled Value.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns the cancel status.
     * @return Value (boolean).
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
