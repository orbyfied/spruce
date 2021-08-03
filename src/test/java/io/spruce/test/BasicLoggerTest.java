package io.spruce.test;

import io.spruce.LoggerFactory;
import io.spruce.arg.LoggerTag;
import io.spruce.prefab.StdLogger;

import java.util.Date;

public class BasicLoggerTest {

    public static void main(String[] args) {
        StdLogger logger = LoggerFactory.stdFactory().make(new LoggerTag("sus"));

        logger.pipeline().addLast(event -> {
            event.text().append(" [" + new Date().toString() + "]");
        });

        logger.info("hello");
    }

}
