package com.yjb.language.java8;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Java 8扩展了注解的适用范围。
 * 从现在开始，绝大部分的东西都可以被注解：局部变量、泛型、超类、和接口实现，甚至可以是方法的异常声明。
 * ElementType.TYPE_USE和ElementType.TYPE_PARAMETER是两个新增加的元素类型，用来描述注解的适用场景。
 * 注解处理API（Annotation Processing API）也做了一些小的改动，以使之能够正确处理这些Java编程语言中新加入的的注解类型。
 */
public class Annotations {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        final Holder<String> holder = new @NonEmpty Holder<String>();
        @NonEmpty Collection<@NonEmpty String> strings = new ArrayList<>();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    public @interface NonEmpty {
    }

    public static class Holder<@NonEmpty T> extends @NonEmpty Object {
        public void method() throws @NonEmpty Exception {
        }
    }
}