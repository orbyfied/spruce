package io.sprucetest;

import io.spruce.LoggerFactory;
import io.spruce.Spruce;
import io.spruce.arg.LogLevel;
import io.spruce.arg.RemoveCapability;
import io.spruce.event.Record;
import io.spruce.pipeline.Pipeline;
import io.spruce.pipeline.part.Handler;
import io.spruce.standard.StandardLogger;
import io.spruce.system.Capability;
import io.spruce.util.color.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class BasicLoggerTest {

    private static final File testFile = Tests.getTestFile("basic_logger_test.log");

    public static void main(String[] args) {
        try {
            long t1 = System.nanoTime();

            // create logger
            StandardLogger logger = LoggerFactory.standard().make("id:sus", new FileOutputStream(testFile));

            // add event handler to pipeline
            logger.pipeline().addLast("appendDate", (Handler<Record>) (pipeline, fluid) -> {
                fluid.text().append(ChatColor.YELLOW_FG + " [" + new Date() + "]");
            });

            // log something
            logger.severe("hello1");

            // remove event handler from pipeline
            logger.pipeline().removeByName("appendDate");

            // log something else
            logger.severe("hello2");

            logger.pipeline().in(new Record(logger, "sus", LogLevel.INFO, "fuck"));


            long t2 = System.nanoTime();
            long t  = t2 - t1;

            logger.info("Time taken: " + t + " (" + t / 1000_0000 + "ms)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
