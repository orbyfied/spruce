package io.spruce.pipeline;

public interface LogHandler {
    /**
     * Called when a logger receives a logging request.
     * @param event The interface object for getting the event information
     *              and modifying the final used values.
     */
    void onLog(LogEvent event);
}
