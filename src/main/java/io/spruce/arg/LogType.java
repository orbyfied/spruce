package io.spruce.arg;

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
    /**
     * A nicely formatted tag string.
     */
    String tag;

    /**
     * Internal ID for possible translation and theme
     * functionality.
     */
    String id;

    /**
     * Extra information/data usable for custom and
     * default logger implementations.
     */
    List<Object> data;

    /** Default constructor. */
    public LogType(String id, String tag, Object... data) {
        this.id   = id;
        this.tag  = tag;
        this.data = Arrays.asList(data);

        if (allById.containsKey(id))
            throw new IllegalArgumentException("LogLevel with the specified ID already exists.");
        allById.put(id, this);
    }

    /*                */
    /* Basic getters. */
    /*                */

    public String       getId()   { return id; }
    public String       getTag()  { return tag; }
    public List<Object> getData() { return data; }

    /**
     * Removes the LogLevel from the registry
     * and destroys all data that the object holds.
     * (Marks it for garbage collection.)
     */
    public void destroy() {
        // remove from registry
        allById.remove(this.id);

        // 'destroy' object data
        this.id   = null;
        this.tag  = null;
        this.data = null;
    }

    /** Should the level be formatted? */
    public enum HasFormatting {
        YES,
        NO;
    }
}
