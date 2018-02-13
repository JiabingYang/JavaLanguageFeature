package com.yjb.language.bit;


public class Binary {

    public static void main(String[] args) {
        test1();
        test2();
    }

    private static void test1() {
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(-1 >> 1)); // 算术移位 高位用符号位补充
        System.out.println(-1 >> 1); // 算术移位 高位用符号位补充
        System.out.println(Integer.toBinaryString(-1 >>> 1)); // 逻辑右移 高位用0补充
        System.out.println(-1 >>> 1); // 逻辑右移 高位用0补充
        System.out.println(-1 / 2); // 逻辑右移 高位用0补充
    }

    private static void test2() {
        System.out.println(Integer.toBinaryString(0)); // 32个1
        System.out.println(Integer.toBinaryString(~0)); // 32个1
    }
}
