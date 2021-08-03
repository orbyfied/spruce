package io.spruce.arg;

import java.util.Arrays;
import java.util.List;

public enum LogLevel {
    SEVERE ("lvl.severe", "Severe", HasFormatting.YES), // error or severe warning
    WARN   ("lvl.warn",   "Warn"  , HasFormatting.YES), // warning
    INFO   ("lvl.info",   "Info"  , HasFormatting.YES), // information
    RAW    ("lvl.raw",    "Raw"   , HasFormatting.NO);  // raw output

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
    LogLevel(String id, String tag, Object... data) {
        this.id   = id;
        this.tag  = tag;
        this.data = Arrays.asList(data);
    }

    /*                */
    /* Basic getters. */
    /*                */

    public String       getId()   { return id; }
    public String       getTag()  { return tag; }
    public List<Object> getData() { return data; }

    /** Should the level be formatted? */
    public enum HasFormatting {
        YES,
        NO;
    }
}
