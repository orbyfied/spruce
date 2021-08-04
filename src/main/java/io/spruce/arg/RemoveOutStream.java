package io.spruce.arg;

import java.io.OutputStream;

public class RemoveOutStream {
    private OutputStream stream;
    public RemoveOutStream(OutputStream stream) { this.stream = stream; }
    public OutputStream getStream() { return stream; }
}
