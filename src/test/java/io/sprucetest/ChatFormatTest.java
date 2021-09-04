package io.sprucetest;

import io.spruce.util.color.ChatColor;

public class ChatFormatTest {
    public static void main(String[] args) {
        // print something in red and underlined
        System.out.println(ChatColor.RED_FG + "" + ChatColor.UNDERLINE + "hi"); // 'AnsiAttr.c(AnsiAttr...)' concatenates multiple ANSI codes.

        // print something in full rgb and bold
        System.out.println(new ChatColor(123, 134, 189) + "" + ChatColor.BOLD + "hello"); // just some random RGB values

        // reset
        System.out.print(ChatColor.RESET);

        // all colors
        String g = ChatColor.BOLD.concat(new ChatColor(255, 120, 120));
        for (int i = 0; i < 16; i++) {
            // get number
            int n = i > 7 ? 90 + i - 7 : 30 + i;

            // create color
            ChatColor color = new ChatColor(n);

            // print
            System.out.println(ChatColor.RED_BG + "N:" + ChatColor.RESET + " " + g + n +
                    ChatColor.RESET + " | " + color + "suspicos");
        }
    }
}
