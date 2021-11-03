package io.sprucetest;

import io.orbyfied.spruce.Spruce;
import io.orbyfied.spruce.arg.DisableCapability;
import io.orbyfied.spruce.system.Capability;

import java.io.File;
import java.io.IOException;

public class Tests {
    public static final File testDir = new File(".test");

    static {
        // initialize spruce
        new Spruce(new DisableCapability(Capability.NATIVES));

        // create test directory
        try {
            if (!testDir.exists())
                testDir.mkdir();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void activate() { }

    public static File getTestFile(String path) {
        File file   = new File(testDir.getAbsolutePath() + "/" + path);
        File parent = file.getParentFile();

        try {
            if (!parent.exists())
                if (!parent.mkdirs())
                    throw new IllegalStateException();
            if (!file.exists())
                if (!file.createNewFile())
                    throw new IllegalStateException();
        } catch (IOException e) { e.printStackTrace(); }

        return file;
    }
}
