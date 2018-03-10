package com.yjb.language.java8;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Date;

/**
 * Test cases are modified from here:
 * https://my.oschina.net/benhaile/blog/193956
 */
public class Jsr310 {

    public static void main(String[] args) {
    }

    public static void testClock() throws InterruptedException {
        System.out.println("----- testClock -----");
        //时钟提供给我们用于访问某个特定 时区的 瞬时时间、日期 和 时间的。
        Clock c1 = Clock.systemUTC(); //系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        System.out.println("0: " + c1.millis()); //每次调用将返回当前瞬时时间（UTC）

        Clock c2 = Clock.systemDefaultZone(); //系统默认时区时钟（当前瞬时时间）

        Clock c31 = Clock.system(ZoneId.of("Europe/Paris")); //巴黎时区
        System.out.println("1: " + c31.millis()); //每次调用将返回当前瞬时时间（UTC）
        Clock c32 = Clock.system(ZoneId.of("Asia/Shanghai"));//上海时区
        System.out.println("2: " + c32.millis());//每次调用将返回当前瞬时时间（UTC）

        Clock c4 = Clock.fixed(Instant.now(), ZoneId.of("Asia/Shanghai"));//固定上海时区时钟
        System.out.println("3: " + c4.millis());
        Thread.sleep(1000);
        System.out.println("4: " + c4.millis()); //不变 即时钟时钟在那一个点不动

        Clock c5 = Clock.offset(c1, Duration.ofSeconds(2)); //相对于系统默认时钟两秒的时钟
        System.out.println("5: " + c1.millis());
        System.out.println("6: " + c5.millis());
    }

    public static void testInstant() {
        System.out.println("----- testInstant -----");
        //瞬时时间 相当于以前的System.currentTimeMillis()
        Instant instant1 = Instant.now();
        System.out.println("0: " + instant1.getEpochSecond());//精确到秒 得到相对于1970-01-01 00:00:00 UTC的一个时间
        System.out.println("1: " + instant1.toEpochMilli()); //精确到毫秒

        Clock clock1 = Clock.systemUTC(); //获取系统UTC默认时钟
        Instant instant2 = Instant.now(clock1);//得到时钟的瞬时时间
        System.out.println("2: " + instant2.toEpochMilli());

        Clock clock2 = Clock.fixed(instant1, ZoneId.systemDefault()); //固定瞬时时间时钟
        Instant instant3 = Instant.now(clock2);//得到时钟的瞬时时间
        System.out.println("3: " + instant3.toEpochMilli());//equals instant1
    }

    public static void testLocalDateTime() {
        System.out.println("----- testLocalDateTime -----");
        //使用默认时区时钟瞬时时间创建 Clock.systemDefaultZone() -->即相对于 ZoneId.systemDefault()默认时区
        LocalDateTime now = LocalDateTime.now();
        System.out.println("0: " + now);

        //自定义时区
        LocalDateTime now2 = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println("1: " + now2);//会以相应的时区显示日期

        //自定义时钟
        Clock clock = Clock.system(ZoneId.of("Asia/Dhaka"));
        LocalDateTime now3 = LocalDateTime.now(clock);
        System.out.println("2: " + now3);//会以相应的时区显示日期

        //不需要写什么相对时间 如java.util.Date 年是相对于1900 月是从0开始
        //2013-12-31 23:59
        LocalDateTime d1 = LocalDateTime.of(2013, 12, 31, 23, 59);

        //年月日 时分秒 纳秒
        LocalDateTime d2 = LocalDateTime.of(2013, 12, 31, 23, 59, 59, 11);

        //使用瞬时时间 + 时区
        Instant instant = Instant.now();
        LocalDateTime d3 = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        System.out.println("3: " + d3);

        //解析String--->LocalDateTime
        LocalDateTime d4 = LocalDateTime.parse("2013-12-31T23:59");
        System.out.println("4: " + d4);
        LocalDateTime d5 = LocalDateTime.parse("2013-12-31T23:59:59.999");//999毫秒 等价于999000000纳秒
        System.out.println("5: " + d5);

        //使用DateTimeFormatter API 解析 和 格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime d6 = LocalDateTime.parse("2013/12/31 23:59:59", formatter);
        System.out.println("6: " + formatter.format(d6));

        //时间获取
        System.out.println("7: " + d6.getYear());
        System.out.println("8: " + d6.getMonth());
        System.out.println("9: " + d6.getDayOfYear());
        System.out.println("10: " + d6.getDayOfMonth());
        System.out.println("11: " + d6.getDayOfWeek());
        System.out.println("12: " + d6.getHour());
        System.out.println("13: " + d6.getMinute());
        System.out.println("14: " + d6.getSecond());
        System.out.println("15: " + d6.getNano());

        //时间增减
        LocalDateTime d7 = d6.minusDays(1);
        LocalDateTime d8 = d7.plus(1, IsoFields.QUARTER_YEARS);
        //LocalDate 即年月日 无时分秒
        //LocalTime即时分秒 无年月日
        //API和LocalDateTime类似就不演示了
    }

    public static void testZonedDateTime() {
        System.out.println("----- testZonedDateTime -----");
        //即带有时区的date-time 存储纳秒、时区和时差（避免与本地date-time歧义）。
        //API和LocalDateTime类似，只是多了时差(如2013-12-20T10:35:50.711+08:00[Asia/Shanghai])
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("0: " + now);
        ZonedDateTime now2 = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println("1: " + now2);
        //其他的用法也是类似的 就不介绍了
        ZonedDateTime z1 = ZonedDateTime.parse("2013-12-31T23:59:59Z[Europe/Paris]");
        System.out.println("2: " + z1);
    }

    public static void testDuration() {
        System.out.println("----- testDuration -----");
        //表示两个瞬时时间的时间段
        Duration d1 = Duration.between(Instant.ofEpochMilli(System.currentTimeMillis() - 12323123), Instant.now());
        //得到相应的时差
        System.out.println("0: " + d1.toDays());
        System.out.println("1: " + d1.toHours());
        System.out.println("2: " + d1.toMinutes());
        System.out.println("3: " + d1.toMillis());
        System.out.println("4: " + d1.toNanos());
        //1天时差 类似的还有如ofHours()
        Duration d2 = Duration.ofDays(1);
        System.out.println("5: " + d2.toDays());
    }

    public static void testChronology() {
        System.out.println("----- testChronology -----");
        //提供对java.util.Calendar的替换，提供对年历系统的支持
        Chronology c = HijrahChronology.INSTANCE;
        ChronoLocalDateTime d = c.localDateTime(LocalDateTime.now());
        System.out.println("0: " + d);
    }

    /**
     * 新旧日期转换
     */
    public static void testNewOldDateConversion() {
        System.out.println("----- testNewOldDateConversion -----");
        Instant instant = new Date().toInstant();
        Date date = Date.from(instant);
        System.out.println("0: " + instant);
        System.out.println("1: " + date);
    }

}
