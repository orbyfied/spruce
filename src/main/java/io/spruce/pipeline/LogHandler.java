package io.spruce.pipeline;

public interface LogHandler<EVENT extends Event> {
    /**
     * Called when a logger receives a logging request.
     * @param event The interface object for getting the event information
     *              and modifying the final used values.
     */
    void onEvent(EVENT event);
}
