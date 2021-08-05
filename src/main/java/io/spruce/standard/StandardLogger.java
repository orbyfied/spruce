package io.spruce.standard;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;
import io.spruce.pipeline.event.LogEvent;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StandardLogger extends Logger {

    private List<OutputStream> outStreams;
    private boolean            shouldPrintTag = true;

    /*
     * Basic constructors.
     */

    public StandardLogger(String id) {
        this(id, new ArrayList<>());
    }

    public StandardLogger(String id, List<OutputStream> streams) {
        super(id);
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
    protected void write(LogEvent event) {
        // create final string and convert to UTF-8 bytes
        String s = event.text().toString() + "\n";
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
        return (shouldPrintTag && tag != null ? "[" + tag + "]" : "") + "[" + level.getTag().toUpperCase() + "] " + text;
    }
}
