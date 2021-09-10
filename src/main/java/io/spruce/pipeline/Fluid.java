package io.spruce.pipeline;

public class Fluid {
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
     * @return Boolean.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Returns if the event was accepted (it was NOT cancelled)
     * @return Boolean.
     */
    public boolean isAccepted() { return !cancelled; }
}
