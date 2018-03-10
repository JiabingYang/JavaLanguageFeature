package com.yjb.language.java8;

interface Interface1 {
    default void hello() {
        System.out.println("From Interface1");
    }
}

interface Interface2 {
    default void hello() {
        System.out.println("From Interface2");
    }
}

interface Interface11 extends Interface1 {
    default void hello() {
        System.out.println("From Interface11");
    }
}

interface Interface21 extends Interface1 {
    default void hello() {
        System.out.println("From Interface21");
    }
}

/**
 * 冲突说明
 * 1.一个声明在类里面的方法优先于任何默认方法（classes always win）
 * 2.否则，则会优先选取最具体的实现
 */
public class DefaultMethod {

    public static void main(String[] args) {
    }
}

class ClassImplements1 implements Interface1 {
}

// Won't Compile
//class ClassImplements1_2 implements Interface1,Interface2 {
//}

// Won't Compile
//class ClassImplements1_11_12 implements Interface1, Interface11, Interface21 {
//}

class ClassImplements1_11 implements Interface1, Interface11 {
}

class ClassImplements1_11Super implements Interface1, Interface11 {

    @Override
    public void hello() {
//        Interface1.super.hello(); // Won't Compile see: https://stackoverflow.com/questions/29525975/default-methods-and-interfaces-extending-other-interfaces
        Interface11.super.hello();
    }
}

class ClassImplements1_11_21 implements Interface1, Interface11, Interface21 {
    @Override
    public void hello() {
//        Interface1.super.hello(); // Won't Compile see: https://stackoverflow.com/questions/29525975/default-methods-and-interfaces-extending-other-interfaces
        Interface11.super.hello();
        Interface21.super.hello();
    }
}

class ClassExtendsClassImplements1_11_21 extends ClassImplements1_11_21 {
    @Override
    public void hello() {
        super.hello();
//        ClassImplements1_11_21.super.hello(); // Won't Compile
//        Interface1.super.hello(); // Won't Compile
//        Interface11.super.hello(); // Won't Compile
//        Interface21.super.hello(); // Won't Compile
    }
}
