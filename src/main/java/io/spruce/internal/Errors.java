package io.spruce.internal;

public class Errors {
    public static void illegalArgument(String s) {
        throw new IllegalArgumentException(s);
    }
}