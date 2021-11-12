package io.orbyfied.spruce.logging.io;

import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.pipeline.PipelineHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

/**
 * Class for information about an output.
 * Is bound to an output worker to create a functional
 * output for logging.
 */
public class Output implements Cloneable, PipelineHolder<Record> {

    /**
     * Creates a new output builder.
     * @return The builder.
     */
    public static Builder builder() { return new Builder(); }

    /**
     * <code>System.out</code> default console output.
     */
    public static final Output SYSOUT = new Builder()
            .hasAnsi(true)
            .withStream(System.out)
            .build();

    /**
     * Output which discards any information written to it.
     */
    public static final Output VOIDING = new Builder()
            .hasAnsi(true)
            .withStream(new VoidingOutputStream())
            .build();

    /**
     * Creates a new output from a <code>FileOutputStream</code>
     * created from the provided file.
     * @param file The file to open.
     * @return The output.
     */
    public static Output ofFile(File file) {
        try {
            return new Builder()
                    .hasAnsi(false)
                    .withStream(new FileOutputStream(file))
                    .build();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    /////////////////////////////////

    /**
     * Does the output stream have ANSI support?
     * True is faster, as it doesn't have to strip the string
     * of ANSI colors, which is why <code>SYSOUT</code> and
     * <code>VOIDING</code> set this to true.
     */
    boolean hasAnsi;

    /**
     * The output stream to write to.
     */
    OutputStream stream;

    /**
     * The event pipeline.
     */
    Pipeline<Record> pipeline;

    /**
     * The initial processor function.
     */
    BiFunction<Record, String, String> processFunction;

    /** Constructor. */
    Output(OutputStream stream,
           boolean hasAnsi,
           BiFunction<Record, String, String> processFunction,
           Pipeline<Record> pipeline) {
        this.stream   = stream;
        this.hasAnsi  = hasAnsi;
        this.pipeline = pipeline != null ? pipeline : new Pipeline<>(this);

        this.processFunction = processFunction;
    }

    /**
     * Writes a string to the output stream.
     * @param str The string to write.
     */
    public void write(String str) {
        try {
            stream.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) { e.printStackTrace(); }
    }

    /* Getters. */
    public boolean                            hasAnsi()            { return hasAnsi; }
    public OutputStream                       getStream()          { return stream; }
    public BiFunction<Record, String, String> getProcessFunction() { return processFunction; }

    /**
     * Clones this object.
     * @return The clone.
     */
    public Output clone() {
        try {
            return (Output) super.clone();
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Constructs a builder of this object.
     * @return The builder.
     */
    public Builder toBuilder() {
        Builder builder = new Builder();

        builder.hasAnsi = hasAnsi;
        builder.stream = stream;
        builder.processFunction = processFunction;
        builder.pipeline = pipeline;

        return builder;
    }

    /* Pipelines. */
    @Override public Pipeline<Record> pipeline() { return pipeline; }
    @Override public void pipeline(Pipeline<Record> pipeline) { this.pipeline = pipeline.cloneFor(this); }

    /////////////////////////////////

    /**
     * Builder for outputs.
     */
    public static class Builder {
        private boolean hasAnsi;
        private OutputStream stream;
        private BiFunction<Record, String, String> processFunction;
        private Pipeline<Record> pipeline;

        public Builder() {
            this.hasAnsi = false;
        }

        public Builder hasAnsi(boolean v) {
            this.hasAnsi = v;
            return this;
        }

        public boolean hasAnsi() {
            return hasAnsi;
        }

        public Builder withStream(OutputStream stream) {
            this.stream = stream;
            return this;
        }

        public OutputStream withStream() {
            return stream;
        }

        public Builder withProcessFunction(BiFunction<Record, String, String> processFunction) {
            this.processFunction = processFunction;
            return this;
        }

        public BiFunction<Record, String, String> withProcessFunction() {
            return processFunction;
        }

        public Pipeline<Record> withPipeline() {
            return pipeline;
        }

        public Builder withPipeline(Pipeline<Record> pipeline) {
            this.pipeline = pipeline;
            return this;
        }

        public Output build() {
            return new Output(
                stream, hasAnsi, processFunction, pipeline
            );
        }
    }

    /**
     * Output stream which discards all data written to it.
     */
    static class VoidingOutputStream extends OutputStream {
        @Override
        public void write(int b) { }
    }

}
