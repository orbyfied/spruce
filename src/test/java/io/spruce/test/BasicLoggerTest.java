package io.spruce.test;

import io.spruce.LoggerFactory;
import io.spruce.prefab.StandardLogger;
import io.spruce.util.color.arg.Space;
import io.spruce.util.color.attributes.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class BasicLoggerTest {

    private static final File testFile = Tests.getTestFile("basic_logger_test.log");

    public static void main(String[] args) {
        System.out.println(
            ChatColor.RED.fg() + "hi"
        );
        try {
            // create logger
            StandardLogger logger = LoggerFactory.stdFactory().make("id:sus", new FileOutputStream(testFile));

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
