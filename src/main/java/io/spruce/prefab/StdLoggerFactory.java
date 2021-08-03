package io.spruce.prefab;

import io.spruce.LoggerFactory;
import io.spruce.pipeline.LogHandler;
import io.spruce.pipeline.LoggerPipeline;
import io.spruce.pipeline.event.LogEvent;

import java.util.List;

public class StdLoggerFactory extends LoggerFactory<StdLogger> {

    public static final StdLoggerFactory pInstance = new StdLoggerFactory();

    @Override
    protected StdLogger new0(String tag) {
        return new StdLogger(tag);
    }

    @Override
    protected void apply0(StdLogger logger, List<LogHandler<LogEvent>> handlerList, List<Object> other) {

    }

}
