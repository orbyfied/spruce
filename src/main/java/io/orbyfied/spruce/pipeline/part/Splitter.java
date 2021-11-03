package io.orbyfied.spruce.pipeline.part;

import io.orbyfied.spruce.pipeline.Part;
import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.pipeline.Fluid;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Splitter<F extends Fluid> implements Part<F> {
    private Pipeline<F>[] pipelines;
    private ThreadPoolExecutor executor;
    private long timeout;
    private boolean async = true;

    /** On a microsecond scale. */
    public static final long DEFAULT_EXECUTOR_TIMEOUT = 1;

    public Splitter(Pipeline<F>... pipelines) {
        this(pipelines, DEFAULT_EXECUTOR_TIMEOUT, false);
    }

    public Splitter(long timeout, Pipeline<F>... pipelines) {
        this(pipelines, timeout, false);
    }

    public Splitter(boolean async, long timeout, Pipeline<F>... pipelines) { this(pipelines, timeout, async); }

    public Splitter(boolean async, Pipeline<F>... pipelines) { this(pipelines, DEFAULT_EXECUTOR_TIMEOUT, async); }

    public Splitter(Pipeline<F>[] pipelines, long timeout, boolean async) {
        this.pipelines = pipelines;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(pipelines.length);
        this.timeout = timeout;
        this.async = async;
    }

    @Override
    public void accept(Pipeline<F> pipeline, F fluid) {
        for (Pipeline<F> pipe : pipelines) {
            executor.execute(() -> {
                pipe.in(fluid);
            });
        }

        try {
            if (!async)
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
