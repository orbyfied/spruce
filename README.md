# spruce (v1.0-a1)
An advanced Java logging framework, with custom loggers, multiple output streams, color & style formatting,
useful utilities for colors and console output and more.

> README will be updated with useful information once the project gets close to being stable.
> **This project is under development, the code may be incomplete or even uncompilable.**

## How to import **`spruce`**

In order to use spruce in your prject, you will first have to download or build a JAR.
Once you have the JAR, relocate it to a folder that is easliy accessible from your project 
For example, in the projects root directory;
```
project 
  - spruce.jar
  - build.gradle
  ...
```

**Next, you will need to import the JAR as a library or dependency.**

With **Gradle**, this is pretty straight forward:
Just put this inside of the `dependencies` closure within your build.gradle
```groovy
implementation(files("(maybe relative) path to spruce jar"))
```
For other build tools/platforms, like Maven, just search it on Google.

## How to use **`spruce`**

Spruce has a many notable features, but I will only show the following in this section:
  * Basic logging (with pipeline)
  * Custom loggers
  * Coloured text

### Basic Logging (With pipeline)

Lets write a simple program that logs stuff to both the console, and a file 
This is actually relatively simple to do using `spruce`:

```java
public class Test {
   public static void main(String[] args) throws IOException {
      // create a new file reference
      // for simplicity, this code assumes that the file alreday exists
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
      LoggerPipeline<LogEvent> pipeline = logger.pipeline();

      // add an event handler that handles the specified 'LogEvent'
      pipeline.addLast(event -> {
         // event.text is a StringBuilder containing the
         // formatted string. the result of this builder
         // at the end of the pipeline is whats written
         // to all of the outputs. this line adds the
         // current date and time to the end of the line
         event.text().append(" [" + new Date() + "]");
      });

      //
      // test if the setup works (it should!)
      //

      logger.info("hello");
   }
}
```


