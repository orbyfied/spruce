package io.spruce.test;

public class RandomTests {
    public static void main(String[] args) {

        long s = 0b01111111111111111111111111111111111111111111111111111111111111111L;
        System.out.println(s);
        System.out.println(s & 0b111111);
        System.out.println(s & -0b111111);

    }
}
