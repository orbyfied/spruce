![Logo](/project/resources/spruce_logo_full.png)
# spruce (v1.0-a1)
![License](https://img.shields.io/github/license/orbyfied/spruce)
![Open Issues](https://img.shields.io/github/issues-raw/orbyfied/spruce?color=%23ffc412)
![Language Count](https://img.shields.io/github/languages/count/orbyfied/spruce)
![Top Language](https://img.shields.io/github/languages/top/orbyfied/spruce?color=%23c8ff12)

An advanced Java logging framework, with custom loggers, multiple output streams, color & style formatting,
useful utilities for colors and console output and more.

> README will be updated with useful information once the project gets close to being stable.
> **This project is under development, the code may be incomplete or even uncompilable.**

## How to import **`spruce`**

In order to use spruce in your project, you will first have to download or build a JAR.
Once you have the JAR, relocate it to a folder that is easliy accessible from your project.
For example, in the projects root directory;
```
root (project folder)
  └ spruce.jar
  └ build.gradle
  ...
```

**Next, you will need to import the JAR as a library or dependency.**

With **Gradle**, this is pretty straight forward:
Just put this inside of the `dependencies` closure within your build.gradle
```groovy
implementation(files("(maybe relative) path to spruce jar"))
```
For other build tools/platforms, like Maven, you will have to search it on Google.
These build tools/platforms will be added in future updates to the README, but
are currently out of scope as I will focus on the README after fixing up the repository
and finishing the main parts of the project.

## How to use **`spruce`**

Spruce has a many notable features, but I will only show the following in this section:
  * Basic logging (with pipeline)
  * Custom loggers
  * Coloured (and formatted) text
  
But before that, you will **always** have to initialize Spruce.
This is as simple as calling the Spruce constructor, without having to store
the instance. You're not required to give it parameters, but you can do some
pretty useful/powerful stuff using it, for example; set the default pipeline that
all loggers will use upon creation, or set the default set of output streams
for any `StandardLogger` that will be created. *More settings will be added in the
future.*

Now lets see how to do this;
```java
public class Test {
    public static void main(String[] args) {
        // without parameters
        new Spruce();

        // with a default handler
        new Spruce(event -> { /* your code */ });

        // removing the stdout stream and adding a ByteArrayOutputStream.
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        new Spruce(s, new RemoveOutStream(System.out));
    }
}
```

### Basic Logging (With pipeline)

Lets write a simple program that logs stuff to both the console and a file 
This is actually relatively simple to do using `spruce`:

```java
public class Test {
   public static void main(String[] args) throws IOException {
      // of course, first initialize Spruce
      new Spruce();

      // create a new file reference
      // for simplicity, this code assumes that the file already exists
      File logFile = new File("log_" + new Date());

      // create a new standard logger
      StandardLogger logger = LoggerFactory.stdFactory().make(
          new FileOutputStream(logFile), // add the file as an additional output
          "tag:Test" // set the loggers "tag"
      );

      // retrieve the pipeline, a loggers pipeline
      // is basically a string of event handlers made
      // to execute events called by the logger once,
      // for example, a message is sent.
      Pipeline<Record> pipeline = logger.pipeline();

      // add an event handler that handles the specified 'Record' (Log event)
      logger.pipeline().addLast("appendDate", (Handler<Record>) (pipeline, event) -> {
          // event.text is a StringBuilder containing the
          // formatted string. the result of this builder
          // at the end of the pipeline is whats written
          // to all of the outputs. this line adds the
          // current date and time to the end of the line
          event.text().append(ChatColor.YELLOW_FG + " [" + new Date() + "]");
      });

      //
      // test if the setup works (it should!)
      //

      logger.info("hello");
   }
}
```

### Custom Loggers

With `spruce` it is really easy to modify existent loggers or create your own from scratch.
If you want to modify an existent logger (like, for example, the `StandardLogger`), you will need to extend it and override
some methods. It is also recommended to create a `LoggerFactory` for your logger, but I won't get into that right now. 
If you want to make your own logger from scratch you will need to extend the abstract class `Logger`.

**Lets see both things in code form.**

```java
// main class
public class Main {
    public static void main(String[] args) {
        // init Spruce
        new Spruce();

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
    public void write(Record event) {
        // write " [HELLO]" to the end of the final text
        event.text().append(" [HELLO]");

        // call StandardLogger's write method to keep its functionality
        super.write(event);
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
```

### Coloured (and formatted) Text

The next one is really simple, and we already saw a bit of it
in the previous example, but `spruce` allows you to simply color
text using ANSI. It allows for formatting, 4-bit color (16), 8-bit color (256)
and even 24-bit color (full RGB).

**This feature is not stable yet, so no example is currently available.**


