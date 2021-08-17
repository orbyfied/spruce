package io.spruce.test;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;
import io.spruce.event.Record;
import io.spruce.standard.StandardLogger;
import io.spruce.util.color.ChatColor;

public class CustomLoggerTest {
    public static void main(String[] args) {
        // construct loggers
        MyLogger1 l1 = new MyLogger1("1");
        MyLogger2 l2 = new MyLogger2("2");

        // test
        l1.info("hello");
        l2.info("hello");

        // EXPECTED:

        /*
        [INFO] hello [HELLO]
        [Info] hello                 <--- THIS SHOULD BE IN RED
         */
    }
}

// method 1: override existent logger
class MyLogger1 extends StandardLogger { // StandardLogger is the 'prefab' logger class shipped with spruce
    public MyLogger1(String id) {
        super(id); // requires super constructor
    }

    @Override
    public void write0(Record event) {
        // write " [HELLO]" to the end of the final text
        event.text().append(" [HELLO]");

        // call StandardLogger's write method to keep its functionality
        super.write0(event);
    }
}

// method 2: logger from scratch
class MyLogger2 extends Logger {
    public MyLogger2(String id) {
        super(id); // requires super constructor
    }

    // as Logger is abstract, we need to implement some methods to make this work
    // we need to implement the following 2 methods;
    //   'void write(LogEvent)' - this is supposed to write whatever information the LogEvent
    //                       passed to any OutputStream.
    //   'String formatPrimary(String, LogLevel, Object...)' - this is supposed to format
    //                                                         the raw text with the corresponding properties
    //                                                         before it is sent to the LogEvent and pipeline.

    @Override
    protected void write0(Record event) {
        // we will just print the text to the console, but colored in red
        System.out.println(ChatColor.RED_FG + event.text().toString());
    }

    @Override
    protected String format0(String text, LogLevel lvl, Object... args) {
        // for this example we will just return the logging level with the text
        return "[" + lvl.getTag() + "] " + text;
    }
}
