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

public class Output implements Cloneable, PipelineHolder<Record> {

    public static Builder builder() { return new Builder(); }

    public static final Output SYSOUT = new Builder()
            .hasAnsi(true)
            .withStream(System.out)
            .build();

    public static final Output VOIDING = new Builder()
            .hasAnsi(true)
            .withStream(new VoidingOutputStream())
            .build();

    public static final Output fromFile(File file) {
        try {
            return new Builder()
                    .hasAnsi(false)
                    .withStream(new FileOutputStream(file))
                    .build();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    /////////////////////////////////

    boolean          hasAnsi;
    OutputStream     stream;
    Pipeline<Record> pipeline;

    BiFunction<Record, String, String> processFunction;

    Output(OutputStream stream,
           boolean hasAnsi,
           BiFunction<Record, String, String> processFunction,
           Pipeline<Record> pipeline) {
        this.stream   = stream;
        this.hasAnsi  = hasAnsi;
        this.pipeline = pipeline != null ? pipeline : new Pipeline<>(this);

        this.processFunction = processFunction;
    }

    public void write(String text) {
        try {
            stream.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public boolean hasAnsi() {
        return hasAnsi;
    }

    public OutputStream getStream() {
        return stream;
    }

    public BiFunction<Record, String, String> getProcessFunction() {
        return processFunction;
    }

    public Output clone() {
        try {
            return (Output) super.clone();
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();

        builder.hasAnsi = hasAnsi;
        builder.stream = stream;
        builder.processFunction = processFunction;
        builder.pipeline = pipeline;

        return builder;
    }

    @Override
    public Pipeline<Record> pipeline() {
        return pipeline;
    }

    @Override
    public void pipeline(Pipeline<Record> pipeline) {
        this.pipeline = pipeline.cloneFor(this);
    }

    /////////////////////////////////

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

    static class VoidingOutputStream extends OutputStream {
        @Override
        public void write(int b) { }
    }

}
