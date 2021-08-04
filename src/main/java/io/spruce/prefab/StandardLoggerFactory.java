package io.spruce.prefab;

import io.spruce.LoggerFactory;
import io.spruce.arg.RemoveOutStream;
import io.spruce.pipeline.LogHandler;
import io.spruce.pipeline.LoggerPipeline;
import io.spruce.pipeline.event.LogEvent;

import java.io.OutputStream;
import java.util.List;

public class StandardLoggerFactory extends LoggerFactory<StandardLogger> {

    /* ID stuff. */
    private static int nid = 0;
    private static int nextId() {
        return nid++;
    }

    /** Public instance of this factory. */
    public static final StandardLoggerFactory pInstance = new StandardLoggerFactory();

    @Override
    protected StandardLogger new0(String id) {
        // generate id if null
        if (id == null) id = Integer.toString(nextId());

        // create a new logger object
        return new StandardLogger(id);
    }

    @Override
    protected void apply0(StandardLogger logger, List<LogHandler<LogEvent>> handlerList, String tag, List<Object> other) {
        // process basic parameters
        logger.pipeline(new LoggerPipeline<>(handlerList));
        logger.setTag(tag);

        // process 'other' parameters
        for (Object arg : other) {

            if (arg instanceof OutputStream)
                logger.addOutStream((OutputStream) arg);
            else if (arg instanceof RemoveOutStream)
                logger.removeOutStream(((RemoveOutStream) arg).getStream());

        }
    }

}
