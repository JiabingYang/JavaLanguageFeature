package com.yjb.language.time;

import java.util.TimeZone;

public class GetDefaultTimeZone {

    public static void main(String[] args) {
        TimeZone timeZone = TimeZone.getDefault();
        String id = timeZone.getID(); //获取时区id
        String name = timeZone.getDisplayName(); //获取名字
        int rawOffset = timeZone.getRawOffset(); //获取时差，返回值毫秒
        System.out.println("TimeZone ID = " + id);
        System.out.println("TimeZone Name = " + name);
        System.out.println("TimeZone RawOffset = " + rawOffset + "ms");
        rawOffset /= 1000;// 转换成秒
        System.out.println("TimeZone RawOffset = " + rawOffset / 3600 + "h"
                + (rawOffset % 3600) / 60 + "m"
                + (rawOffset % 3600) % 60 + "s");
    }
}
