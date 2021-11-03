package io.orbyfied.spruce.util.color;

/**
 * Represents an
 */
public abstract class AnsiAttr {

    /**
     * Returns/generates the ANSI escape
     * code of the attribute.
     * @return The escape code.
     */
    public abstract String code(Object... args);

    @Override public String toString() { return Ansi.PREFIX + code() + Ansi.SUFFIX; }

    /**
     * Concatenates this color/formatting code with
     * the others specified.
     * @param attrs The ANSI codes.
     * @return The concatenated string.
     */
    public String concat(AnsiAttr... attrs) {
        StringBuilder builder = new StringBuilder(this.toString());
        for (AnsiAttr attr : attrs)
            builder.append(attr);
        return builder.toString();
    }

    /**
     * @see AnsiAttr#concat(AnsiAttr...)
     */
    public String c(AnsiAttr... colors) { return concat(colors); }
}
