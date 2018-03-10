package com.yjb.language.java8;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class OptionalTest {

    public static void main(String[] args) {

        // test1
        System.out.println("----- test1 -----");
        Optional<String> fullName = Optional.ofNullable(null);
        System.out.println("Full Name is set? " + fullName.isPresent());
        System.out.println("Full Name: " + fullName.orElseGet(() -> "[none]"));
        System.out.println(fullName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));

        // test2
        System.out.println("----- test2 -----");
        Optional<String> firstName = Optional.of("Tom");
        System.out.println("First Name is set? " + firstName.isPresent());
        System.out.println("First Name: " + firstName.orElseGet(() -> "[none]"));
        System.out.println(firstName.map(s -> "Hey " + s + "!").orElse("Hey Stranger!"));
        System.out.println();

        // test3
        // 通过利用 Java 8 的 Optional 类型来摆脱 null 检查
        Optional.of(new Outer())
                .map(Outer::getNested)
                .map(Nested::getInner)
                .map(Inner::getFoo)
                .ifPresent(System.out::println);

        // test3
        // 通过利用一个 supplier 函数来解决嵌套路径的问题
        Outer obj = new Outer();
        resolve(() -> obj.getNested().getInner().getFoo())
                .ifPresent(System.out::println);
    }

    private static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}

class Outer {
    Nested nested;

    Nested getNested() {
        return nested;
    }
}

class Nested {
    Inner inner;

    Inner getInner() {
        return inner;
    }
}

class Inner {
    String foo;

    String getFoo() {
        return foo;
    }
}
