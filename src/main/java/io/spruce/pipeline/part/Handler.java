package io.spruce.pipeline.part;

import io.spruce.pipeline.Fluid;
import io.spruce.pipeline.Part;
import io.spruce.pipeline.Pipeline;

public interface Handler<F extends Fluid> extends Part<F> {
    @Override default boolean added(Pipeline<F> pipeline) { return true; }
    @Override default void removed(Pipeline<F> pipeline) { }
}
