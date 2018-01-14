package com.yjb.language.classinit;

import java.util.Collection;

/**
 * Created by Joe on 2017/3/7.
 */
public class SubInitClass2 extends InitClass2 {

    static {
        System.out.println("运行子类静态代码");
    }

    public static Field2 f2 = new Field2();

}
