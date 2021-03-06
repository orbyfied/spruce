package io.orbyfied.spruce.standard;

import io.orbyfied.spruce.util.color.TextFormat;
import io.orbyfied.spruce.logging.Logger;
import io.orbyfied.spruce.logging.LogType;
import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.io.OutputWorker;

import java.util.ArrayList;
import java.util.List;

public class StandardLogger extends Logger {

    private List<OutputWorker> outStreams;
    private boolean            shouldPrintTag = true;

    /*
     * Basic constructors.
     */

    public StandardLogger(String id) {
        this(id, new ArrayList<>());
    }

    public StandardLogger(String id, List<OutputWorker> streams) {
        super(id);
        this.outStreams = streams;
    }

    /*                                     */
    /* Basic output stream list modifiers. */
    /*                                     */

    public List<OutputWorker> getOutStreams() { return outStreams; }
    public void setOutStreams(List<OutputWorker> outStreams) { this.outStreams = outStreams; }
    public void addOutStream(OutputWorker stream) { this.outStreams.add(stream); }
    public void removeOutStream(OutputWorker stream) { this.outStreams.remove(stream); }

    @Override
    protected List<OutputWorker> write0(Record record) {
        return outStreams;
    }

    @Override
    protected String format0(String text, LogType level, Object... extra) {
        // create string builder options
        StringBuilder builder = new StringBuilder();

        builder.append("[");

        // append tag (if needed)
        if (shouldPrintTag && tag != null) builder
                .append(TextFormat.DARK_GRAY_FG)
                .append(tag)
                .append(TextFormat.RESET)
                .append("/");

        // append log level
        builder.append(TextFormat.BOLD);
        switch (level.getId()) {
            case "std.severe":
                builder.append(TextFormat.DARK_RED_FG).append("SEVERE");
                break;
            case "std.info":
                builder.append(TextFormat.DARK_BLUE_FG).append("INFO");
                break;
            case "std.warn":
                builder.append(TextFormat.DARK_YELLOW_FG).append("WARN");
                break;
            default:
                builder.append(new TextFormat(160, 160, 160)).append(level.getTag().toUpperCase());
        }

        builder.append(TextFormat.RESET).append("] ");

        // append text
        builder.append(text);

        // return
        return builder.toString();
    }
}
