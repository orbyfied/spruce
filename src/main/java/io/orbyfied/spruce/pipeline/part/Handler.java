package io.orbyfied.spruce.pipeline.part;

import io.orbyfied.spruce.pipeline.Part;
import io.orbyfied.spruce.pipeline.Pipeline;
import io.orbyfied.spruce.pipeline.Fluid;

public interface Handler<F extends Fluid> extends Part<F> {
    @Override default boolean added(Pipeline<F> pipeline) { return true; }
    @Override default void removed(Pipeline<F> pipeline) { }
}
