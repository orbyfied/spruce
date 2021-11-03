package io.orbyfied.spruce.standard;

import io.orbyfied.spruce.logging.LoggerFactory;
import io.orbyfied.spruce.Spruce;
import io.orbyfied.spruce.arg.RemoveOutWorker;
import io.orbyfied.spruce.logging.io.OutputWorker;
import io.orbyfied.spruce.pipeline.Part;
import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.event.Record;

import java.util.List;

public class StandardLoggerFactory extends LoggerFactory<StandardLogger> {

    /* ID stuff. */
    private static int nid = 0;
    private static int nextId() {
        return nid++;
    }

    /** Public instance of this factory. */
    private static StandardLoggerFactory instance;
    public static StandardLoggerFactory get() { return instance; }

    @Override
    protected StandardLogger new0(String id) {
        // generate id if null
        if (id == null) id = Integer.toString(nextId());

        // create a new logger object
        return new StandardLogger(id);
    }

    @Override
    protected void apply0(StandardLogger logger, List<Part<Record>> handlerList, String tag, List<Object> other) {
        // process basic parameters
        logger.pipeline(new Pipeline<>(handlerList));
        logger.setTag(tag);

        // process 'other' parameters
        for (Object arg : other) {

            if (arg instanceof OutputWorker)
                logger.addOutStream((OutputWorker) arg);
            else if (arg instanceof RemoveOutWorker)
                logger.removeOutStream(((RemoveOutWorker) arg).getWorker());

        }

        // add default output streams
        logger.getOutStreams().addAll(Spruce.get().defaultOutputStreams);
    }

}
