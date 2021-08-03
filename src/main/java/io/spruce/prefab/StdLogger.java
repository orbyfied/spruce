package io.spruce.prefab;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StdLogger extends Logger {

    private List<OutputStream> outStreams;

    /*
     * Basic constructors.
     */

    public StdLogger(String tag) {
        this(tag, new ArrayList<>());
    }

    public StdLogger(String tag, List<OutputStream> streams) {
        super(tag);
        this.outStreams = streams;
        outStreams.add(System.out); // add system.out as a default output stream
    }

    /*                                     */
    /* Basic output stream list modifiers. */
    /*                                     */

    public List<OutputStream> getOutStreams() { return outStreams; }
    public void setOutStreams(List<OutputStream> outStreams) { this.outStreams = outStreams; }
    public void addOutStream(OutputStream stream) { this.outStreams.add(stream); }
    public void removeOutStream(OutputStream stream) { this.outStreams.remove(stream); }

    @Override
    protected void write(String str) {
        // create final string and convert to UTF-8 bytes
        String s = str + "\n";
        byte[] d = s.getBytes(StandardCharsets.UTF_8);

        // TODO: add event pipelines per output stream
        // write to all output streams
        for (OutputStream stream : outStreams) {
            try {
                stream.write(d);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected String formatPrimary(String text, LogLevel level, Object... extra) {
        return "[" + level.getTag().toUpperCase() + "] " + text;
    }
}
