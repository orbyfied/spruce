package io.sprucetest;

import io.spruce.logging.LoggerFactory;
import io.spruce.arg.LogType;
import io.spruce.event.Record;
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

            for (int i = 0; i < 1000; i++)
                logger.info(i);

            long t2 = System.nanoTime();
            long t  = t2 - t1;

            logger.info("Time taken: " + t + " (" + t / 1000_0000 + "ms)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
