package io.spruce.prefab;

import io.spruce.Logger;
import io.spruce.arg.LogLevel;

public class StdLogger extends Logger {
    /**
     * Basic constructor.
     */
    public StdLogger(String tag) {
        super(tag);
    }

    @Override
    protected void write(String str) {
        System.out.println(str);
    }

    @Override
    protected String formatPrimary(String text, LogLevel level, Object... extra) {
        return "[" + level.getTag().toUpperCase() + "] " + text;
    }
}
