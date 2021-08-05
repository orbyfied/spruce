package io.spruce.util.color;

import io.spruce.internal.ArrayUtils;
import io.spruce.util.color.attributes.ChatColor;

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
     * @param colors The ANSI codes.
     * @return The concatenated string.
     */
    public String concat(AnsiAttr... colors) {
        return Ansi.encode(ArrayUtils.merge(
                new AnsiAttr[] { this }, colors));
    }

    /**
     * @see AnsiAttr#concat(AnsiAttr...)
     */
    public String c(AnsiAttr... colors) { return concat(colors); }
}
