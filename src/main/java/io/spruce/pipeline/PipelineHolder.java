package io.spruce.pipeline;

public interface PipelineHolder<F extends Fluid> {
    Pipeline<F> pipeline();
    void pipeline(Pipeline<F> pipeline);
    default boolean eventPassed(F event) { return true; }
}
