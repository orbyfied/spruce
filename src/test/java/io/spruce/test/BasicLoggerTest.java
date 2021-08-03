package io.spruce.test;

import io.spruce.Logger;
import io.spruce.LoggerFactory;
import io.spruce.arg.LoggerTag;
import io.spruce.pipeline.LogHandler;
import io.spruce.pipeline.event.LogEvent;
import io.spruce.prefab.StdLoggerFactory;

import java.util.Date;

public class BasicLoggerTest {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.stdFactory().make(new LoggerTag("sus"));

        logger.pipeline().addLast(event -> {
            event.text().append(" [" + new Date().toString() + "]");
        });

        logger.info("hello");
    }

}
