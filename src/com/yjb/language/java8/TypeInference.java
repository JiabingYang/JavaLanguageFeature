package com.yjb.language.java8;

public class TypeInference {
    public static void main(String[] args) {
        final Value<String> value = new Value<>();
        // Value.defaultValue()的参数类型可以被编译器推断得出而不需显式声明。
        // 在Java 7中，同样的代码编译不通过，需要更改为Value.<String>defaultValue()。
        value.getOrDefault("22", Value.defaultValue());
    }
}

class Value<T> {
    public static <T> T defaultValue() {
        return null;
    }

    public T getOrDefault(T value, T defaultValue) {
        return (value != null) ? value : defaultValue;
    }
}