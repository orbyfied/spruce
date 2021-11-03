package io.orbyfied.spruce.event;

import io.orbyfied.spruce.logging.Logger;
import io.orbyfied.spruce.pipeline.Fluid;

public class LoggerEvent extends Fluid {
    protected Logger logger;

    public LoggerEvent(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }
}
