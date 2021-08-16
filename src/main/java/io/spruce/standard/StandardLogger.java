package io.spruce.standard;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;
import io.spruce.event.Record;
import io.spruce.util.color.Ansi;
import io.spruce.util.color.attributes.ChatColor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
    }

    /*                                     */
    /* Basic output stream list modifiers. */
    /*                                     */

    public List<OutputStream> getOutStreams() { return outStreams; }
    public void setOutStreams(List<OutputStream> outStreams) { this.outStreams = outStreams; }
    public void addOutStream(OutputStream stream) { this.outStreams.add(stream); }
    public void removeOutStream(OutputStream stream) { this.outStreams.remove(stream); }

    @Override
    protected void write0(Record event) {
        // create final string and convert to UTF-8 bytes
        String s  = event.prefix()          +
                    event.text().toString() +
                    event.suffix()          +
                    ChatColor.RESET         + "\n";
        String s1 = Ansi.strip(s);
        byte[] d  = s.getBytes(StandardCharsets.UTF_8);
        byte[] d1 = s1.getBytes(StandardCharsets.UTF_8);

        // TODO: add event pipelines per output stream
        // write to all output streams
        for (OutputStream stream : outStreams) {
            try {
                if (!(stream instanceof PrintStream))
                     stream.write(d1);
                else stream.write(d);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected String format0(String text, LogLevel level, Object... extra) {
        // create string builder options
        StringBuilder builder = new StringBuilder();

        // append tag (if needed)
        if (shouldPrintTag && tag != null) builder.append("(").append(ChatColor.BOLD).append(tag)
                .append(ChatColor.RESET).append(")").append(" ");

        // append log level
        builder.append(ChatColor.BOLD + "[");

        switch (level) {
            case SEVERE:
                builder.append(ChatColor.RED_FG + "SEVERE");
                break;
            case INFO:
                builder.append(ChatColor.AQUA_FG + "INFO");
                break;
            case WARN:
                builder.append(ChatColor.YELLOW_FG + "WARN");
                break;
            default:
                builder.append(new ChatColor(160, 160, 160) + level.getTag().toUpperCase());
        }

        builder.append(ChatColor.RESET).append(ChatColor.BOLD + "] ").append(ChatColor.RESET);

        // append text
        builder.append(text);

        // return
        return builder.toString();
    }
}
