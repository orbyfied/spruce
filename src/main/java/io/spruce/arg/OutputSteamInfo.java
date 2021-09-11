package io.spruce.arg;

import java.io.OutputStream;

public class OutputSteamInfo {
    OutputStream stream;
    boolean hasAnsi;

    public OutputSteamInfo(OutputStream stream, boolean hasAnsi) {
        this.stream = stream;
        this.hasAnsi = hasAnsi;
    }

    public OutputStream getStream() {
        return stream;
    }

    public boolean hasAnsiSupport() {
        return hasAnsi;
    }
}
