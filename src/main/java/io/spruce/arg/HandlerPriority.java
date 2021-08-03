package io.spruce.arg;

/**
 * Priority of a handler in a pipeline.
 * > HASN'T BEEN IMPLEMENTED YET.
 */
public enum HandlerPriority {
    UNDEFINED(0),
    LOWEST(1),
    LOW(2),
    NORMAL(3),
    HIGH(4),
    HIGHEST(5);

    int v;
    HandlerPriority(int v) { this.v = v; }

    public int getPriority() { return v; }

    public static boolean isHigher(HandlerPriority value, HandlerPriority than) {
        return value.v > than.v;
    }
}
