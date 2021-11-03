package io.orbyfied.spruce.util.color.arg;

public enum Space {
    FOREGROUND("38;"),
    BACKGROUND("48;");

    String prefix;
    Space(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() { return prefix; }

    public static Space getFromArray(Object... objects) {
        for (Object o : objects)
            if (o instanceof Space)
                return (Space) o;
        return null;
    }
}
