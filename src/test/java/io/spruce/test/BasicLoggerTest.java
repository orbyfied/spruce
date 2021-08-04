package io.spruce.test;

import io.spruce.LoggerFactory;
import io.spruce.prefab.StdLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class BasicLoggerTest {

    private static final File testFile = Tests.getTestFile("basic_logger_test.log");

    public static void main(String[] args) {
        try {
            // create logger
            StdLogger logger = LoggerFactory.stdFactory().make("id:sus", "tag:Hello", new FileOutputStream(testFile));

            // add event handler to pipeline
            logger.pipeline().addLast(event -> {
                event.text().append(" [" + new Date().toString() + "]");
            });

            // log something
            logger.info("hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
