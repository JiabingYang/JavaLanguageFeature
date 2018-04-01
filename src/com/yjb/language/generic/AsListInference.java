package com.yjb.language.generic;

import java.util.*;

/**
 * 示例修改自《Java编程思想》
 */
public class AsListInference {
    public static void main(String[] args) {
        List<Snow> snow1 = Arrays.asList(new Crusty(), new Slush(), new Powder());
        Arrays.asList(new Light(), new Heavy());
        List<Snow> snow3 = new ArrayList<>();
        Collections.addAll(snow3, new Light(), new Heavy());
        LinkedList<Snow> snows = new LinkedList<>(Arrays.asList(new Snow[5]));
        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        for (String temp : a) {
            if ("2".equals(temp)) {
                a.remove(temp);
            }
        }
    }
}

class Snow {
}

class Powder extends Snow {
}

class Light extends Powder {
}

class Heavy extends Powder {
}

class Crusty extends Snow {
}

class Slush extends Snow {
}
