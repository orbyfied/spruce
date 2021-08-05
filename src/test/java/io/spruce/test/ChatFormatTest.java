package io.spruce.test;

import io.spruce.util.color.attributes.ChatColor;

public class ChatFormatTest {
    public static void main(String[] args) {
        // print something in red and underlined
        System.out.println(ChatColor.UNDERLINE.c(ChatColor.RED_FG) + "hi"); // 'AnsiAttr.c(AnsiAttr...)' concatenates multiple ANSI codes.

        // print something in full rgb and bold
        System.out.println(new ChatColor(123, 134, 189) + "hello"); // just some random RGB values

        // reset
        System.out.print(ChatColor.RESET);
    }
}
