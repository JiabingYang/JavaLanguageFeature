package com.yjb.language.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例修改自《Java编程思想》
 */
public class New {
    public static <K, V> Map<K, V> map(K key, V value) {
        HashMap<K, V> hashMap = new HashMap<K, V>();
        hashMap.put(key, value);
        return hashMap;
    }

    public static <K, V> Map<K, V> map() {
        return new HashMap<K, V>();
    }

//    public static void f(Map<String, Integer> map) {
//        System.out.println(map);
//    }

    public static void f(Map<Snow, ? extends Snow> map) {
        System.out.println(map);
        for (Snow value : map.values()) {
            System.out.println(value.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
//        f(New.map());
//        f(New.map(new Powder(), new Powder()));
//        System.out.println(New.map());
//        System.out.println(New.map("strKey", 1));
        byte a = 127;
        byte b = 127;
//        a += b;
        System.out.println((byte) (a + b));
    }
}
