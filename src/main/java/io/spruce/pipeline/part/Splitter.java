package io.spruce.pipeline.part;

import io.spruce.pipeline.Fluid;
import io.spruce.pipeline.Part;
import io.spruce.pipeline.Pipeline;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Splitter<F extends Fluid> implements Part<F> {
    private Pipeline<F>[] pipelines;
    private ThreadPoolExecutor executor;
    private long timeout;

    /** On a microsecond scale. */
    public static final long DEFAULT_EXECUTOR_TIMEOUT = 1;

    public Splitter(Pipeline<F>... pipelines) {
        this(pipelines, DEFAULT_EXECUTOR_TIMEOUT);
    }

    public Splitter(long timeout, Pipeline<F>... pipelines) {
        this(pipelines, timeout);
    }

    public Splitter(Pipeline<F>[] pipelines, long timeout) {
        this.pipelines = pipelines;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(pipelines.length);
        this.timeout = timeout;
    }

    @Override
    public void reach(Pipeline<F> pipeline, F fluid) {
        for (Pipeline<F> pipe : pipelines) {
            executor.execute(() -> {
                pipe.in(fluid);
            });
        }

        try {
            executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public boolean added(Pipeline<F> pipeline) {
        return true;
    }

    @Override
    public void removed(Pipeline<F> pipeline) {

    }
}
