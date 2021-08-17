package io.spruce.internal;

public class Errors {
    /** IllegalArgument */
    public static void ia(String s) {
        throw new IllegalArgumentException(s);
    }
}
