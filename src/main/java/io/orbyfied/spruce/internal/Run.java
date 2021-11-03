package io.orbyfied.spruce.internal;

public class Run {
    public static boolean tryRun(Runnable runnable) {
        if (runnable == null) return false;
        try { runnable.run(); } catch (Exception e) {
            e.printStackTrace(); return false;
        }

        return true;
    }
}
