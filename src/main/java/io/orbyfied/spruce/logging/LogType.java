package io.orbyfied.spruce.logging;

import io.orbyfied.spruce.event.Record;
import io.orbyfied.spruce.logging.process.Formatter;
import io.orbyfied.spruce.logging.type.Debug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: thinking of renaming this to "LogType"
public class LogType {

    /**
     * All LogLevels mapped by ID.
     */
    private static Map<String, LogType> allById = new HashMap<>();

    public static LogType getById(String id) { return allById.get(id); }

    public static final LogType
        SEVERE = new LogType("std.severe", "Severe", HasFormatting.YES); // error or severe warning
    public static final LogType
        WARN = new LogType("std.warn", "Warn", HasFormatting.YES); // warning
    public static final LogType
        INFO = new LogType("std.info", "Info", HasFormatting.YES); // information
    public static final LogType
        RAW = new LogType("std.raw", "Raw", HasFormatting.NO); // raw message

    public static final LogType
            DEBUG = new LogType("std.debug", "Debug", record -> {
                if (record.getObject() instanceof Debug.Struct)
                    record.text().append(record.getObject());

                for (Object o : record.getOther())
                    if (o instanceof Debug.Struct)
                        record.text().append(o);
            }, HasFormatting.YES); // debug

    /**
     * A nicely formatted tag string.
     */
    final String tag;

    /**
     * Internal ID for possible translation and theme
     * functionality.
     */
    final String id;

    /**
     * Extra information/data usable for custom and
     * default logger implementations.
     */
    final List<Object> data;

    /**
     * Optional formatter. Gets called by the logger
     * worker asynchronously. Optional.
     */
    final Formatter formatter;

    /**
     * Default constructor.
     * @param id The ID of this log type.
     * @param tag The tag of this log type.
     * @param formatter The formatter. (Optional)
     * @param data The extra info. (Optional)
     */
    public LogType(String id, String tag, Formatter formatter, Object... data) {
        this.id        = id;
        this.tag       = tag;
        this.data      = Arrays.asList(data);
        this.formatter = formatter;

        if (allById.containsKey(id))
            throw new IllegalArgumentException("LogLevel with the specified ID already exists.");
        allById.put(id, this);
    }

    /**
     * Concise constructor: Formatter inlined to null.
     */
    public LogType(String id, String tag, Object... data) {
        this(id, tag, null, data);
    }

    /*                */
    /* Basic getters. */
    /*                */

    public String       getId()        { return id; }
    public String       getTag()       { return tag; }
    public List<Object> getData()      { return data; }
    public Formatter    getFormatter() { return formatter; }

    /**
     * Removes the LogLevel from the registry
     * and destroys all data that the object holds.
     * (Marks it for garbage collection.)
     */
    public void destroy() {
        // remove from registry
        allById.remove(this.id);
    }

    /**
     * Formats the record using this LogType's formatter.
     * Does nothing and returns false if it is null, otherwise
     * returns true.
     * @param record The record to format.
     * @return If the formatting was successful.
     */
    public boolean format(Record record) {
        if (formatter == null)
            return false;
        try { formatter.format(record); } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Should the level be formatted? */
    public enum HasFormatting {
        YES,
        NO;
    }
}
