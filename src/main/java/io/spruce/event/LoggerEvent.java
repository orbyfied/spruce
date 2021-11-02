package io.spruce.event;

import io.spruce.logging.Logger;
import io.spruce.pipeline.Fluid;

public class LoggerEvent extends Fluid {
    protected Logger logger;

    public LoggerEvent(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
