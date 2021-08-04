package io.spruce.util.color.attributes;

import io.spruce.util.color.Ansi;
import io.spruce.util.color.AnsiAttr;
import io.spruce.util.color.arg.ColorType;
import io.spruce.util.color.arg.Space;

import java.awt.*;

public class ChatColor extends ColorAttr implements Cloneable {

    /* Background and foreground sequence prefixes. */
    private static final String BG_P = Space.BACKGROUND.getPrefix();
    private static final String FG_P = Space.FOREGROUND.getPrefix();

    /* True and 8-bit color sequence prefixes. */
    private static final String TRUE_COLOR_P = "2;";
    private static final String EBIT_COLOR_P = "5;";

    private boolean   isLiteral;
    private int       literal;
//    private ColorType colorType;

    /**
     * Is the specified color a background color?
     */
    private Space space;

    /*               */
    /* Constructors. */
    /*               */

    ChatColor() { super(0, 0, 0); }

    public ChatColor(int c) {
        super(0, 0, 0);
        this.isLiteral = true;
        this.literal   = c;
    }

//    public ChatColor(int c, ColorType type) {
//        super(0, 0, 0);
//        this.isLiteral = true;
//        this.literal   = c;
//        this.colorType = type;
//    }

    public ChatColor(char c, Space space) {
        super(c);
        this.space = space;
    }

    public ChatColor(int r, int g, int b, Space space) {
        super(r, g, b);
        this.space = space;
    }

    public ChatColor(Color color, Space space) {
        super(color);
        this.space = space;
    }

    /**
     * Generates the sequence prefix.
     * Returns nothing if literal, otherwise it returns
     * The background/foreground prefix with if it is
     * a true color or not.
     */
    @Override
    public String seqPrefix(Object... args) {
        if (isLiteral) return "";

        // get space
        Space space = Space.getFromArray(args);
        if (space == null) space = this.space;

        // get color depth and return
        return space.getPrefix() + (isRGB() ? TRUE_COLOR_P : EBIT_COLOR_P);
    }

    /**
     * Generate the code. If it is literal, it modifies the
     * literal based on if it is a background color, and
     * returns it, otherwise it returns the super method.
     */
    @Override public String code(Object... args) {
        // retrieve space
        Space space = Space.getFromArray(args);
        if (space == null) space = this.space;

        // check if it is literal
        if (isLiteral) {
            int l = literal + (space == Space.BACKGROUND ? 10 : 0);
            return Integer.toString(l);
        }

        // return super code
        return super.code(args);
    }

    public ChatColor clone() {
        // create new color
        ChatColor color = new ChatColor();

        // copy fields
        color.space = this.space;

        color.col   = this.col;
        color.color = this.color;

        color.isLiteral = this.isLiteral;
        color.literal   = this.literal;

        // return
        return color;
    }

    /**
     * Clones the object with the specified space
     * applied.
     * @param space The specified space.
     * @return The cloned object.
     */
    public ChatColor space(Space space) {
        ChatColor color = this.clone();
        color.space = space;
        return color;
    }

    /** @see ChatColor#space(Space) */
    public ChatColor bg() {
        return space(Space.BACKGROUND);
    }

    /** @see ChatColor#space(Space) */
    public ChatColor fg() {
        return space(Space.FOREGROUND);
    }

    /////////////////////////////////////////////////////////////////////////////////

    public static final ChatColor RESET = new ChatColor(0);

                public static final ChatColor BOLD      = new ChatColor(1);
    @Deprecated public static final ChatColor FAINT     = new ChatColor(2);
    @Deprecated public static final ChatColor ITALIC    = new ChatColor(3);
                public static final ChatColor UNDERLINE = new ChatColor(4);
                public static final ChatColor FRAMED    = new ChatColor(51);
                public static final ChatColor ENCIRCLED = new ChatColor(52);
                public static final ChatColor OVERLINED = new ChatColor(53);


    public static final ChatColor RED = new ChatColor(31);
        public static final ChatColor RED_BG = RED.bg();
}
