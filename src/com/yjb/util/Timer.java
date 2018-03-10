package com.yjb.util;

public class Timer {

    public static long testNano(Action0 action0) {
        long start = System.nanoTime();
        action0.call();
        return System.nanoTime() - start;
    }

    public static long testMilli(Action0 action0) {
        long start = System.currentTimeMillis();
        action0.call();
        return System.currentTimeMillis() - start;
    }

    public static long printlnTestNano(String tag, Action0 action0) {
        long result = testNano(action0);
        Printer.println(tag, result + " ns");
        return result;
    }

    public static long printlnTestMilli(String tag, Action0 action0) {
        long result = testMilli(action0);
        Printer.println(tag, result + " ms");
        return result;
    }

}
