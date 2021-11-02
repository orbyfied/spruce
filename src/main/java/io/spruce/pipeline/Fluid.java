package io.spruce.pipeline;

import java.util.HashMap;
import java.util.Map;

public class Fluid {
    /**
     * Has the event been cancelled?
     */
    protected boolean cancelled = false;

    /**
     * Data carried by this fluid.
     */
    protected Map<String, Object> carry = new HashMap<>();

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

    /*
     * Carrier system. Allows you to carry values throughout
     * the event chain that the log request goes through.
     */

    public <T> void carry(final String key, final T value) {
        carry.put(key, value);
    }

    public <T> T carried(final String key) {
        return (T) carry.get(key);
    }

    public <T> T uncarry(final String key) {
        return (T) carry.remove(key);
    }

    public void uncarry(final Object o) {
        carry.forEach((key, value) -> {
            if (value == o) carry.remove(key);
        });
    }
}
