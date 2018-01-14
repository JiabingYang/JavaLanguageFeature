package com.yjb.language.classinit;

public class InitClass2 {

    static {
        System.out.println("运行父类静态代码");
    }

    public static Field1 f1 = new Field1();
    public static Field1 f2;

}
