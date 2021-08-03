package io.spruce.prefab;

import io.spruce.LoggerFactory;
import io.spruce.pipeline.LogHandler;
import io.spruce.pipeline.LoggerPipeline;

import java.util.List;

public class StdLoggerFactory extends LoggerFactory<StdLogger> {

    @Override
    protected StdLogger new0(String tag) {
        return new StdLogger(tag);
    }

    @Override
    protected void apply0(StdLogger logger, List<LogHandler> handlerList, List<Object> other) {
        logger.pipeline(new LoggerPipeline(handlerList));
    }

}
