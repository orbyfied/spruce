package io.spruce.standard;

import io.spruce.LoggerFactory;
import io.spruce.Spruce;
import io.spruce.arg.RemoveOutStream;
import io.spruce.pipeline.Handler;
import io.spruce.pipeline.Pipeline;
import io.spruce.event.Record;

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
    protected void apply0(StandardLogger logger, List<Handler<Record>> handlerList, String tag, List<Object> other) {
        // process basic parameters
        logger.pipeline(new Pipeline<>(handlerList));
        logger.setTag(tag);

        // process 'other' parameters
        for (Object arg : other) {

            if (arg instanceof OutputStream)
                logger.addOutStream((OutputStream) arg);
            else if (arg instanceof RemoveOutStream)
                logger.removeOutStream(((RemoveOutStream) arg).getStream());

        }

        // add default output streams
        logger.getOutStreams().addAll(Spruce.getConfigurationInstance().cDefaultOutputStreams);
    }

}
