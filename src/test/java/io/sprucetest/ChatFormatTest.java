package io.sprucetest;

import io.orbyfied.spruce.logging.LoggerFactory;
import io.orbyfied.spruce.Spruce;
import io.orbyfied.spruce.arg.DisableCapability;
import io.orbyfied.spruce.standard.StandardLogger;
import io.orbyfied.spruce.system.Capability;
import io.orbyfied.spruce.util.color.TextFormat;

public class ChatFormatTest {
    public static void main(String[] args) {
        new Spruce(new DisableCapability(Capability.NATIVES));

        StandardLogger logger = LoggerFactory.standard().make();
        logger.info(TextFormat.AQUA_FG + "hi");

        // print something in red and underlined
        System.out.println(TextFormat.RED_FG + "" + TextFormat.UNDERLINE + "hi"); // 'AnsiAttr.c(AnsiAttr...)' concatenates multiple ANSI codes.

        // print something in full rgb and bold
        System.out.println(new TextFormat(123, 134, 189) + "" + TextFormat.BOLD + "hello"); // just some random RGB values

        // reset
        System.out.print(TextFormat.RESET);

        // all colors
        String g = TextFormat.BOLD.concat(new TextFormat(255, 120, 120));
        for (int i = 0; i < 16; i++) {
            // get number
            int n = i > 7 ? 90 + i - 7 : 30 + i;

            // create color
            TextFormat color = new TextFormat(n);

            // print
            System.out.println(TextFormat.RED_BG + "N:" + TextFormat.RESET + " " + g + n +
                    TextFormat.RESET + " | " + color + "suspicos");
        }
    }
}
