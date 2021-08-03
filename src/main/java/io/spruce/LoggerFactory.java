package io.spruce;

import io.spruce.arg.LoggerTag;
import io.spruce.pipeline.LogHandler;
import io.spruce.pipeline.event.LogEvent;
import io.spruce.prefab.StdLoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class LoggerFactory<T extends Logger> {

    public  static       StdLoggerFactory stdFactory() { return StdLoggerFactory.pInstance; }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new instance of T.
     * @param tag The logger's tag.
     * @return The new instance.
     */
    protected abstract T new0(String tag);

    /**
     * Applies the specified parameters to the logger.
     * @param logger The logger object to apply everything to.
     *
     * @param handlerList The list of handlers, meant for the pipeline.
     * @param other Other parameters.
     */
    protected abstract void apply0(T logger, List<LogHandler<LogEvent>> handlerList, List<Object> other);

    /**
     * Creates a new logger of type T.
     * @param args The parameters.
     * @return The new logger.
     */
    public T make(Object... args) {
        // iterate, process and collect arguments
        String                     tag      = null;
        List<LogHandler<LogEvent>> handlers = new ArrayList<>();
        List<Object>               other    = new ArrayList<>();

        for (Object arg : args) {
            // process arg
            if (arg instanceof LoggerTag) tag = ((LoggerTag) arg).s;
            else if (arg instanceof LogHandler) handlers.add((LogHandler<LogEvent>) arg);

            else other.add(arg);
        }

        // make a new instance
        T logger = new0(tag);

        // apply params
        apply0(logger, handlers, other);

        // return final logger object
        return logger;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** UTIL: Quick class check. */
    private boolean uIs(Object o, String cls) { return o.getClass().getSimpleName().equals(cls); }

}
