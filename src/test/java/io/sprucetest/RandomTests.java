package io.sprucetest;

import io.spruce.LoggerFactory;
import io.spruce.Spruce;
import io.spruce.arg.RemoveCapability;
import io.spruce.standard.StandardLogger;
import io.spruce.system.Capability;

public class RandomTests {
    public static void main(String[] args) {
        new Spruce(new RemoveCapability(Capability.NATIVES));

        long t1 = System.nanoTime();

        StandardLogger logger = LoggerFactory.standard().make();

        logger.info("Hey");

//        Pipeline<Record> pipeline = logger.pipeline();
//
//        Pipeline<Record> pipeline1 = new Pipeline<>();
//        Pipeline<Record> pipeline2 = new Pipeline<>();
//
//        pipeline1.addLast((Handler<Record>) (pipeline3, fluid) -> {
//            System.out.println("[ HELLO 1 ] Hello message: " + fluid.text());
//        });
//
//        pipeline2.addLast((Handler<Record>) (pipeline3, fluid) -> {
//            System.out.println("[ HELLO 2 ] Hello SUS: " + fluid.getLevel());
//        });
//
//        pipeline.addLast(new Splitter<>(true, pipeline1, pipeline2));

        logger.info("Hey2");

        for (int i = 0; i < 10000; i++)
            logger.info(i);

        long t2 = System.nanoTime();
        long t  = t2 - t1;

        System.out.println("[i] TIME TAKEN: " + t + "ns (" + t / 1_000_000 + "ms)");
    }
}
