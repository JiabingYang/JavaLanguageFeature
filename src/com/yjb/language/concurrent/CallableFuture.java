package com.yjb.language.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class CallableFuture {

    public static void main(String[] args) {
        try {
            basicTest();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
//        try {
//            timeoutTest();
//        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            e.printStackTrace();
//        }
    }

    private static void basicTest() throws InterruptedException, ExecutionException {
        Callable<Integer> task = () -> {
            System.out.println("task called");
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Integer> future = executor.submit(task);

        System.out.println("future done? " + future.isDone());

        // Future与底层的executor service紧密的结合在一起。
        // 记住，如果你关闭executor，所有的未中止的future都会抛出异常。
//        executor.shutdownNow();
        Integer result = future.get();

        System.out.println("future done? " + future.isDone());
        System.out.print("result: " + result);
    }

    private static void timeoutTest() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            } catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        future.get(1, TimeUnit.SECONDS);
    }

    private static void invokeAllTest() throws InterruptedException {
        // Executors支持通过invokeAll()一次批量提交多个callable。
        // 这个方法结果一个callable的集合，然后返回一个future的列表。
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);
    }

    private static void invokeAnyTest() throws ExecutionException, InterruptedException {
        // 批量提交callable的另一种方式就是invokeAny()，它的工作方式与invokeAll()稍有不同。
        // 在等待future对象的过程中，这个方法将会阻塞直到第一个callable中止然后返回这一个callable的结果。

        // 返回一个ForkJoinPool类型的 executor，它的工作方法与其他常见的execuotr稍有不同。
        // 与使用一个固定大小的线程池不同，ForkJoinPools使用一个并行因子数来创建，默认值为主机CPU的可用核心数。
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));

        String result = executor.invokeAny(callables);
        System.out.println(result);
    }

    private static Callable<String> callable(String result, long sleepSeconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }
}
