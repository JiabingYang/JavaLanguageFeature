package com.yjb.language.java8;

import java.lang.annotation.*;

public class RepeatingAnnotations {
    @Annot("a1")
    @Annot("a2")
    public static void main(String[] args) {
        for (Filter filter : Filterable.class.getAnnotationsByType(Filter.class)) {
            System.out.println(filter.value());
        }
//        System.out.println(Filterable.class.getAnnotation(Filter.class).value()); // NullPointerException
//        System.out.println(Filterable.class.getAnnotation(Filters.class).value());

        Annots annots1 = RepeatingAnnotations.class.getAnnotation(Annots.class);
        System.out.println(annots1.value()[0] + "," + annots1.value()[1]);// 输出: @Annot(value=a1),@Annot(value=a2)

        Annot[] annots2 = RepeatingAnnotations.class.getAnnotationsByType(Annot.class);
        System.out.println(annots2[0] + "," + annots2[1]);// 输出: @Annot(value=a1),@Annot(value=a2)
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filters {
        Filter[] value();
    }

    ;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(Filters.class)
    public @interface Filter {
        String value();
    }

    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    @Retention(RetentionPolicy.RUNTIME)
            //该注解存在于类文件中并在运行时可以通过反射获取
    @interface Annots {
        Annot[] value();
    }

    @Retention(RetentionPolicy.RUNTIME) //该注解存在于类文件中并在运行时可以通过反射获取
    @Repeatable(Annots.class)
    @interface Annot {
        String value();
    }
}