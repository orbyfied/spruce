package io.spruce.logging;

import io.spruce.Spruce;
import io.spruce.meta.Nullable;
import io.spruce.pipeline.Part;
import io.spruce.event.Record;
import io.spruce.pipeline.Pipeline;
import io.spruce.standard.StandardLoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class LoggerFactory<T extends Logger> {

    public static StandardLoggerFactory standard() { return StandardLoggerFactory.get(); }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new instance of T.
     * @param id The logger's id.
     * @return The new instance.
     */
    protected abstract T new0(@Nullable String id);

    /**
     * Applies the specified parameters to the logger.
     * @param logger The logger object to apply everything to.
     *
     * @param handlerList The list of handlers, meant for the pipeline.
     * @param other Other parameters.
     */
    protected abstract void apply0(
            T logger,
            List<Part<Record>> handlerList,
            @Nullable String tag,
            List<Object> other
    );

    /**
     * Creates a new logger of type T.
     * @param args The parameters.
     * @return The new logger.
     */
    public T make(Object... args) {
        // check if the caller is from the system
        boolean _systemRequest = false;
        try { throw new Exception(); }
        catch (Exception e) {
            StackTraceElement[] stack = e.getStackTrace();
            StackTraceElement   el    = stack[1];
            if (el.getClassName().startsWith("io.spruce."))
                _systemRequest = true;
        }

        // check if initialized
        if (!_systemRequest)
            uAssertInitialized();

        // iterate, process and collect arguments
        String                id       = null;
        String                tag      = null;
        List<Part<Record>> handlers = new ArrayList<>();
        List<Object>          other    = new ArrayList<>();

        for (Object arg : args) {
            // process arg
            if (arg instanceof String) {
                String s = (String) arg;
                if      (s.startsWith("id:"))  id = s.substring(3);
                else if (s.startsWith("tag:")) tag = s.substring(4);
            } else if (arg instanceof Part) handlers.add((Part<Record>) arg);

            else other.add(arg);
        }

        // make a new instance
        T logger = new0(id);

        // apply default settings
        Spruce config = Spruce.get();

        Pipeline<Record> d_pipeln = config.defaultPipeline;
        if (d_pipeln != null) logger.pipeline(d_pipeln);

        // apply params
        apply0(logger, handlers, tag, other);

        // return final logger object
        return logger;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** UTIL: Quick check if Spruce has been enabled yet. */
    private static void uAssertInitialized() {
        if (!Spruce.isInitialized()) { throw new IllegalStateException("Spruce has not been initialized yet."); }
    }

    /** UTIL: Quick class check. */
    private static boolean uIs(Object o, String cls) { return o.getClass().getSimpleName().equals(cls); }

}
