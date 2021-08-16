package io.spruce.test;

import io.spruce.LoggerFactory;
import io.spruce.standard.StandardLogger;
import io.spruce.util.color.attributes.ChatColor;

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
            logger.pipeline().addLast("appendDate", event -> {
                event.text().append(ChatColor.YELLOW_FG + " [" + new Date() + "]");
            });

            // log something
            logger.severe("hello1");

            // remove event handler from pipeline
            logger.pipeline().removeByName("appendDate");

            // log something else
            logger.severe("hello2");


            long t2 = System.nanoTime();
            long t  = t2 - t1;

            logger.info("Time taken: " + t + " (" + t / 1000_0000 + "ms)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
