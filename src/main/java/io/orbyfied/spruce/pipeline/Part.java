package io.orbyfied.spruce.pipeline;

public interface Part<FLUID extends Fluid> {
    /**
     * Called when a supposed 'fluid' reaches the pipeline part.
     * @param pipeline The pipeline.
     * @param fluid    The fluid.
     */
    void accept(Pipeline<FLUID> pipeline, FLUID fluid);

    /**
     * Called when the part is added to a pipeline.
     * @param pipeline The pipeline.
     * @return If false is returned, the part will be removed again.
     */
    boolean added(Pipeline<FLUID> pipeline);

    /**
     * Called when the part is removed from the pipeline.
     * @param pipeline The pipeline.
     */
    void removed(Pipeline<FLUID> pipeline);
}
