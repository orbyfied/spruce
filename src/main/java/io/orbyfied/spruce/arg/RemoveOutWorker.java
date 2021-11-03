package io.orbyfied.spruce.arg;

import io.orbyfied.spruce.logging.io.OutputWorker;

public class RemoveOutWorker {
    private OutputWorker stream;
    public RemoveOutWorker(OutputWorker worker) { this.stream = worker; }
    public OutputWorker getWorker() { return stream; }
}
