package io.sprucetest;

import io.orbyfied.spruce.logging.io.Output;
import io.orbyfied.spruce.logging.Logger;
import io.orbyfied.spruce.logging.LogType;
import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.io.OutputWorker;
import io.orbyfied.spruce.standard.StandardLogger;
import io.orbyfied.spruce.util.color.TextFormat;

import java.util.List;

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
        this.addOutStream(OutputWorker.create(Output.SYSOUT)); // add System.out output
    }

    @Override
    public List<OutputWorker> write0(Record event) {
        // write " [HELLO]" to the end of the final text
        event.text().append(" [HELLO]");

        // call StandardLogger's write method to keep its functionality
        return super.write0(event);
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

    private static final OutputWorker outputWorker = OutputWorker.create(
            Output.builder()
                    .withStream(System.out)
                    .hasAnsi(true)
                    .withProcessFunction((record, s) -> TextFormat.DARK_RED_FG + s)
                    .build()
    );

    private static final List<OutputWorker> owlist = List.of(outputWorker);

    @Override
    protected List<OutputWorker> write0(Record event) {
        return owlist;
    }

    @Override
    protected String format0(String text, LogType lvl, Object... args) {
        // for this example we will just return the logging level with the text
        return "[" + lvl.getTag() + "] " + text;
    }
}
