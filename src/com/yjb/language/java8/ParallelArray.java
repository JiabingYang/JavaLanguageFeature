package com.yjb.language.java8;

import com.yjb.util.Timer;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ParallelArray {

    public static void main(String[] args) {
        final int LENGTH = 2000000;
        long[] arrayOfLong = new long[LENGTH];
        long[] arrayOfLongAnother = new long[LENGTH];
        long[] arrayOfLongCopy = new long[LENGTH];

        Timer.printlnTestMilli("setAll", () -> Arrays.setAll(arrayOfLongAnother, index -> ThreadLocalRandom.current().nextInt(1000000)));
        Timer.printlnTestMilli("parallelSetAll", () -> Arrays.parallelSetAll(arrayOfLong, index -> ThreadLocalRandom.current().nextInt(1000000)));
//        Arrays.stream(arrayOfLong).limit(10).forEach(i -> System.out.print(i + " "));
//        System.out.println();
        Timer.printlnTestMilli("arrayCopy", () -> System.arraycopy(arrayOfLong, 0, arrayOfLongCopy, 0, LENGTH));

        Timer.printlnTestMilli("StreamSequentialSort", Arrays.stream(arrayOfLong).sequential().sorted()::toArray);
        Timer.printlnTestMilli("sort", () -> Arrays.sort(arrayOfLongCopy));
        Timer.printlnTestMilli("StreamParallelSort", Arrays.stream(arrayOfLong).parallel().sorted()::toArray);
        Timer.printlnTestMilli("parallelSort", () -> Arrays.parallelSort(arrayOfLong));
        Arrays.stream(arrayOfLong).limit(10).forEach(i -> System.out.print(i + " "));
        System.out.println();
        Arrays.stream(arrayOfLongCopy).limit(10).forEach(i -> System.out.print(i + " "));
        System.out.println();
    }
}
