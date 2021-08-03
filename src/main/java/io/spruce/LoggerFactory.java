package io.spruce;

import io.spruce.arg.LoggerTag;
import io.spruce.pipeline.LogHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class LoggerFactory<T extends Logger> {

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
    protected abstract void apply0(T logger, List<LogHandler> handlerList, List<Object> other);

    /** UTIL: Quick class check. */
    private boolean uIs(Object o, String cls) { return o.getClass().getSimpleName().equals(cls); }

    /**
     * Creates a new logger of type T.
     * @param args The parameters.
     * @return The new logger.
     */
    public T make(Object... args) {
        // collect arguments
        String tag                = null;
        List<LogHandler> handlers = new ArrayList<>();
        List<Object>     other    = new ArrayList<>();

        int l = args.length;
        for (int i = 0; i < l; i++) {
            // get arg
            Object arg = args[i];

            // process arg
            if      (arg instanceof LoggerTag)  tag = ((LoggerTag) arg).s;
            else if (arg instanceof LogHandler) handlers.add((LogHandler) arg);

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
}
