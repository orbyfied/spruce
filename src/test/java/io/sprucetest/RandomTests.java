package io.sprucetest;

import io.orbyfied.spruce.Spruce;
import io.orbyfied.spruce.arg.DisableCapability;
import io.orbyfied.spruce.system.Capability;

public class RandomTests {
    public static void main(String[] args) {
        new Spruce(new DisableCapability(Capability.NATIVES));

        long t1 = System.nanoTime();

        //////////////////////////////////

        new Spruce();

        //////////////////////////////////

        long t2 = System.nanoTime();
        long t  = t2 - t1;

        System.out.println("[i] TIME TAKEN: " + t + "ns (" + t / 1_000_000 + "ms)");
    }
}
