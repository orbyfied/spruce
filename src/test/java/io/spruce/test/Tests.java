package io.spruce.test;

import java.io.File;
import java.io.IOException;

public class Tests {
    public static final File testDir = new File(".test");

    static {
        try {
            testDir.mkdir();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static File getTestFile(String path) {
        File file   = new File(testDir.getAbsolutePath() + "/" + path);
        File parent = file.getParentFile();

        try {
            if (!parent.exists())
                parent.mkdirs();
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) { e.printStackTrace(); }

        return file;
    }
}
