package com.yjb.language.java8;

import java.util.function.Function;

public class FunctionTest {

    private static String a = "123";

    public static String op(Function<Integer, String> function0, Function<String, Integer> function1) {
        return function0.compose(function1).apply(a);
    }

    public static void main(String[] args) {
        op(integer -> String.valueOf(integer), s -> Integer.valueOf(s));
    }
}
