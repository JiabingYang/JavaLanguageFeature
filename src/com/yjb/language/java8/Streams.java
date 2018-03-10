package com.yjb.language.java8;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java 8 引入了流式操作（Stream），通过该操作可以实现对集合（Collection）的并行处理和函数式操作。
 * <p>
 * Stream操作分为“中间操作”和“终端操作”。最终操作返回一特定类型的结果，而中间操作返回流本身，这样就可以将多个操作依次串联起来。
 * <p>
 * “中间操作”会返回一个新的Stream。这些操作总是延迟执行的，当执行一个中间操作比如filter时，并不会真的执行任何过滤操作；
 * 而是创建一个新的Stream，当这个新的Stream被遍历时，它里面会包含原来Stream中与参数中指定的谓词（Predicate）匹配的那些元素。
 * 该操作会保持 stream 处于中间状态，允许做进一步的操作。它返回的还是的 Stream，允许更多的链式操作。
 * 常见的中间操作有：
 * filter()：对元素进行过滤；
 * sorted()：对元素排序；
 * map()：元素的映射；
 * distinct()：去除重复元素；
 * subStream()：获取子 Stream 等。
 * <p>
 * “终端操作”，如forEach或者sum，则会遍历stream从而产生结果或者附带结果。
 * 在终端操作执行完毕后，Stream的管道会被视为已经消费完毕，不可再次使用。
 * 在大多数情况下，终端操作是“贪婪的”，总是能够即时完成数据的遍历操作。
 * 该操作必须是流的最后一个操作，一旦被调用，Stream 就到了一个终止状态，而且不能再使用了。
 * 常见的终止操作有：
 * forEach()：对每个元素做处理；
 * toArray()：把元素导出到数组；
 * findFirst()：返回第一个匹配的元素；
 * anyMatch()：是否有匹配的元素等。
 * <p>
 * 根据流的并发性，流又可以分为串行和并行两种。流式操作实现了集合的过滤、排序、映射等功能。
 * 串行流上的操作是在一个线程中依次完成，而并行流则是在多个线程上同时执行。
 * 并行与串行的流可以相互切换：通过 stream.sequential() 返回串行的流，通过 stream.parallel() 返回并行的流。
 * 相比较串行的流，并行的流可以很大程度上提高程序的执行效率。
 * <p>
 * Stream 和 Collection 集合的区别：
 * Collection 是一种静态的内存数据结构，而 Stream 是有关计算的。前者是主要面向内存，存储在内存中，后者主要是面向 CPU，通过 CPU 实现计算。
 */
public class Streams {

    public static void main(String[] args) {
        final Collection<Task> tasks = Arrays.asList(
                new Task(Status.OPEN, 5),
                new Task(Status.OPEN, 13),
                new Task(Status.CLOSED, 8)
        );
        totalPointsOfOpenTasks(tasks);
        final double totalPoints = getTotalPoints(tasks);
        groupTasksByStatus(tasks);
        getWeightOfEachTask(tasks, totalPoints);

        //串行输出为 1200ms，并行输出为 800ms。可见，并行排序的时间相比较串行排序时间要少不少。
        sequentialSorting();
        parallelSorting();

        compareSequentialParallel();
    }

    private static void compareSequentialParallel() {
        long t0 = System.nanoTime();
        //初始化一个范围100万整数流,求能被2整除的数字，toArray（）是终点方法
        int a[]= IntStream.range(0, 1_000_000).filter(p -> p % 2==0).toArray();
        long t1 = System.nanoTime();

        //和上面功能一样，这里是用并行流来计算
        int b[]= IntStream.range(0, 1_000_000).parallel().filter(p -> p % 2==0).toArray();
        long t2 = System.nanoTime();
        //我本机的结果是serial: 0.06s, parallel 0.02s，证明并行流确实比顺序流快
        System.out.printf("serial: %.2fs, parallel %.2fs%n", (t1 - t0) * 1e-9, (t2 - t1) * 1e-9);
    }

    private static void parallelSorting() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 1000000; i++) {
            double d = Math.random() * 1000;
            list.add(d + "");
        }
        long start = System.nanoTime();//获取系统开始排序的时间点
        int count = (int) ((Stream) list.stream().parallel()).sorted().count();
        long end = System.nanoTime();//获取系统结束排序的时间点
        long ms = TimeUnit.NANOSECONDS.toMillis(end - start);//得到并行排序所用的时间
        System.out.println(ms + "ms");
    }

    private static void sequentialSorting() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            double d = Math.random() * 1000;
            list.add(d + "");
        }
        long start = System.nanoTime();//获取系统开始排序的时间点
        int count = (int) ((Stream) list.stream().sequential()).sorted().count();
        long end = System.nanoTime();//获取系统结束排序的时间点
        long ms = TimeUnit.NANOSECONDS.toMillis(end - start);//得到串行排序所用的时间
        System.out.println(ms + "ms");
    }

    private static void getWeightOfEachTask(Collection<Task> tasks, double totalPoints) {
        // Calculate the weight of each tasks (as percent of total points)
        final Collection<String> result = tasks
                .stream()                                        // Stream<Task>
                .mapToInt(Task::getPoints)                     // IntStream
                .asLongStream()                                  // LongStream
                .mapToDouble(points -> points / totalPoints)   // DoubleStream
                .boxed()                                         // Stream<Double>
                .mapToLong(weigth -> (long) (weigth * 100)) // LongStream
                .mapToObj(percentage -> percentage + "%")      // Stream<String>
                .collect(Collectors.toList());                 // List<String>
        System.out.println(result);
    }

    private static void groupTasksByStatus(Collection<Task> tasks) {
        // Group tasks by their status
        final Map<Status, List<Task>> map = tasks
                .stream()
                .collect(Collectors.groupingBy(Task::getStatus));
        System.out.println(map);
    }

    private static double getTotalPoints(Collection<Task> tasks) {
        // Calculate total points of all tasks
        final double totalPoints = tasks
                .stream()
                .parallel()
                .map(task -> task.getPoints()) // or map(Task::getPoints)
                .reduce(0, Integer::sum);
        System.out.println("Total points (all tasks): " + totalPoints);
        return totalPoints;
    }

    private static void totalPointsOfOpenTasks(Collection<Task> tasks) {
        // Calculate total points of all active tasks using sum()
        final long totalPointsOfOpenTasks = tasks
                .stream()
                .filter(task -> task.getStatus() == Status.OPEN)
                .mapToInt(Task::getPoints)
                .sum();
        System.out.println("Total points: " + totalPointsOfOpenTasks);
    }

    private enum Status {
        OPEN, CLOSED
    }

    private static final class Task {
        private final Status status;
        private final Integer points;

        Task(final Status status, final Integer points) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format("[%s, %d]", status, points);
        }
    }
}
