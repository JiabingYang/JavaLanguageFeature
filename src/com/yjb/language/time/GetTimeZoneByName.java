package com.yjb.language.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GetTimeZoneByName {

    public static void main(String[] args) {

//        不能使用 时区名称来获取 时区，发现EST是准的，但是CST不准。
//        因为CST可以同时表示美国，澳大利亚，中国，古巴四个国家的标准时间：
//
//        Central Standard Time (USA) UT-6:00
//        Central Standard Time (Australia) UT+9:30
//        China Standard Time UT+8:00
//        Cuba Standard Time UT-4:00

        Date date = new Date();

        SimpleDateFormat sdfZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US);

        // 默认 是本地时区
        System.out.println(sdfZ.format(date));
        sdfZ.setTimeZone(TimeZone.getTimeZone("CST"));
        System.out.println(sdfZ.format(date));

        // 美国东部标准时间
        sdfZ.setTimeZone(TimeZone.getTimeZone("EST"));
        System.out.println(sdfZ.format(date));
        sdfZ.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdfZ.format(date));
    }
}
