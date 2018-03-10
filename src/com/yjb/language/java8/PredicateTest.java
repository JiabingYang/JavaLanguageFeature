package com.yjb.language.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
interface PersonInterface {
    boolean test(Person person);

//    default String toString(){return ""};
}

public class PredicateTest {
    private List<Person> persons = new ArrayList<>();

    public static void main(String[] args) {
    }

    public List<Person> getMaleList(PersonInterface filter) {
        List<Person> res = new ArrayList<>();
        persons.forEach(person -> {
            if (filter.test(person)) {//调用 PersonInterface 的方法
                res.add(person);
            }
        });
        return res;
    }

    public List<Person> getMaleList(Predicate<Person> predicate) {
        List<Person> res = new ArrayList<>();
        persons.forEach(person -> {
            if (predicate.test(person)) {//调用 Predicate 的抽象方法 test
                res.add(person);
            }
        });
        return res;
    }
}

class Person {
}