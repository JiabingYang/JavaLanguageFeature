package com.yjb.language.string;

/**
 * String#intern
 * <p>
 * 1. 如果常量池中存在当前字符串：
 * 返回值：就会直接返回当前字符串。
 * 常量池：不做其他操作。
 * <p>
 * 2. 如果常量池中没有此字符串
 * 返回值：会将此字符串放入常量池中后, 再返回。
 * 1.6常量池：方法区的字符串常量池创建对象。
 * 1.7常量池：如果堆中存在该字符串，堆的字符串常量池会创建一个引用指向该对象；否则在堆的字符串常量池中创建对象。
 * <p>
 * 参考：
 * https://tech.meituan.com/in_depth_understanding_string_intern.html
 */
public class StringIntern {

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        // 生成了2个对象。常量池中的“1” 和 JAVA Heap 中的字符串对象
        String s = new String("1");
        // s对象去常量池中寻找后发现 “1” 已经在常量池里了
        s.intern();
        // 生成一个 s2的引用指向常量池中的“1”对象。
        String s2 = "1";
        // 结果就是 s 和 s2 的引用地址明显不同
        System.out.println(s == s2); // false

        // 生成了2最终个对象，是字符串常量池中的“1” 和 JAVA Heap 中的 s3引用指向的对象。
        // 中间还有2个匿名的new String("1")我们不去讨论它们。
        // 此时s3引用对象内容是"11"，但此时常量池中是没有 “11”对象的。
        String s3 = new String("1") + new String("1");
        // 将 s3中的“11”字符串放入 String 常量池中。
        // 因为此时常量池中不存在“11”字符串，因此常规做法是跟 jdk6 图中表示的那样，在常量池中生成一个 "11" 的对象。
        // 关键点是 jdk7 中常量池不在 Perm 区域了，这块做了调整。
        // 常量池中不需要再存储一份对象了，可以直接存储堆中的引用。
        // 这份引用指向 s3 引用的对象。 也就是说引用地址是相同的。
        s3.intern();
        // "11"是显示声明的，因此会直接去常量池中创建。
        // 创建的时候发现已经有这个对象了，此时也就是指向 s3 引用对象的一个引用。
        // 所以 s4 引用就指向和 s3 一样了。
        String s4 = "11";
        // 因此最后的比较 s3 == s4 是 true。
        System.out.println(s3 == s4); // true
    }

    private static void test2() {
        String s = new String("1");
        String s2 = "1"; // 直接从常量池中取地址引用的
        s.intern();
        System.out.println(s == s2); // false

        String s3 = new String("1") + new String("1");
        // 声明 s4 的时候常量池中是不存在“11”对象的，执行完毕后，“11“对象是 s4 声明产生的新对象
        String s4 = "11";
        // 常量池中“11”对象已经存在了，因此 s3 和 s4 的引用是不同的
        s3.intern();
        System.out.println(s3 == s4); // false
    }
}
