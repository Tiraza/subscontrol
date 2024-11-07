package br.com.subscontrol.utils;

public final class ThreadUtils {

    private ThreadUtils() {}

    public static void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException("Error in test sleep", e);
        }
    }
}
