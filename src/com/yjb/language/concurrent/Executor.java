package com.yjb.language.concurrent;

import java.util.concurrent.*;

public class Executor {

    public static void main(String[] args) {
//        basicTest();

        try {
            ScheduledExecutorTest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void ScheduledExecutorTest() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // schedule
        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
        ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);

        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms", remainingDelay);

        // scheduleAtFixedRate
        Runnable taskFixedRate = () -> System.out.println("Scheduling: " + System.nanoTime());
        int initialDelay = 0;
        int period = 1;
        executor.scheduleAtFixedRate(taskFixedRate, initialDelay, period, TimeUnit.SECONDS);

        // scheduleWithFixedDelay
        Runnable taskFixedDelay = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            } catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };
        executor.scheduleWithFixedDelay(taskFixedDelay, 0, 1, TimeUnit.SECONDS);
    }

    private static void basicTest() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        });

        try {
            System.out.println("attempt to shutdown executor");
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("shutdown finished");
        }
    }

}
