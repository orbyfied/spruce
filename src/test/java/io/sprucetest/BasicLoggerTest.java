package io.sprucetest;

import io.spruce.logging.Logger;
import io.spruce.logging.LoggerFactory;
import io.spruce.arg.LogType;
import io.spruce.event.Record;
import io.spruce.pipeline.Pipeline;
import io.spruce.pipeline.part.Handler;
import io.spruce.standard.StandardLogger;
import io.spruce.util.color.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class BasicLoggerTest {

    private static final File testFile = Tests.getTestFile("basic_logger_test.log");

    public static void main(String[] args) {
        try {
            // create logger
            StandardLogger logger = LoggerFactory.standard().make("id:sus", new FileOutputStream(testFile));

            long t1 = System.nanoTime();

            //////////////////

            logger.prePipeline().addLast("getDate", (Handler<Record>) (pipeline, event) -> {
                event.carry("date", new Date());
            });

            logger.pipeline().addLast("appendDate", (Handler<Record>) (pipeline, event) -> {
                event.prefix()
                        .append("[")
                        .append(event.carried("date").toString())
                        .append("] ");
            });

            logger.info("hello");

            //////////////////

            long t2 = System.nanoTime();
            long t  = t2 - t1;

            logger.info("Time taken: " + t + " (" + t / 1000_0000 + "ms)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
