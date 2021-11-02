package io.spruce.arg;

import io.spruce.logging.io.OutputWorker;

import java.io.OutputStream;

public class RemoveOutWorker {
    private OutputWorker stream;
    public RemoveOutWorker(OutputWorker worker) { this.stream = worker; }
    public OutputWorker getWorker() { return stream; }
}
