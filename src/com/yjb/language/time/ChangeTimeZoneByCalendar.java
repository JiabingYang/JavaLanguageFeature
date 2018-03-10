package com.yjb.language.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ChangeTimeZoneByCalendar {

    public static void main(String[] args) {
        // Date、Calendar SimpleDateFormat 默认时区
        Date date = new Date();
        System.out.println(date);

        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd hh:mm:ss z yyy", Locale.US);
        System.out.println(sdf.format(date));

        calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        SimpleDateFormat calendarSdf = new SimpleDateFormat("M.dd", Locale.US);
        System.out.println(calendarSdf.format(calendar.getTime()));
        // 修改时区之后，通过calendar的get方法
        System.out.println((calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
    }
}
