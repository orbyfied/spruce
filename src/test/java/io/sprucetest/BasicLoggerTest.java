package io.sprucetest;

import io.orbyfied.spruce.Spruce;
import io.orbyfied.spruce.arg.OutputInfo;
import io.orbyfied.spruce.logging.LoggerFactory;
import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.io.OutputWorker;
import io.orbyfied.spruce.pipeline.part.Handler;
import io.orbyfied.spruce.standard.StandardLogger;
import io.orbyfied.spruce.util.color.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class BasicLoggerTest {

    private static final File testFile = Tests.getTestFile("basic_logger_test.log");

    public static void main(String[] args) {
        try {
            System.out.println(Spruce.VERSION);

            // create logger
            StandardLogger logger = LoggerFactory.standard().make("id:sus", new FileOutputStream(testFile));

            long t1 = System.nanoTime();

            //////////////////

            logger.prePipeline().addLast("getDate", (Handler<Record>) (pipeline, event) -> {
                event.carry("date", new Date());
            });

            logger.pipeline().addLast("appendDate", (Handler<Record>) (pipeline, event) -> {
                event.prefix()
                        .append(ChatColor.YELLOW_FG)
                        .append("[")
                        .append(event.carried("date").toString())
                        .append("] ")
                        .append(ChatColor.RESET);
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
