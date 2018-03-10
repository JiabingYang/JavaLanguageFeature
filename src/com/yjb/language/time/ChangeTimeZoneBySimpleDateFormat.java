package com.yjb.language.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChangeTimeZoneBySimpleDateFormat {

    public static void main(String[] args) {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.US);
        // 默认 是本地时区
        System.out.println(sdf.format(date));

        // GMT 时间
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        System.out.println(sdf.format(date));

        // 美国东部标准时间
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        System.out.println(sdf.format(date));

        // 中国标准时间
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(sdf.format(date));
    }
}
