package ru.Frozik6k;

import ru.Frozik6k.collection.MyHashMap;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        MyHashMap<Integer, Integer> map = new MyHashMap<>();
        map.put(1, 1);
        map.put(127, 2);
        map.put(255, 3);
        map.put(2, 4);
        map.put(128, 5);
        map.put(256, 6);
        map.put(3, 7);
        map.put(129, 8);
        map.put(257, 9);

        System.out.println(map.get(1) + " " + map.get(2) + " " + map.get(3));
        System.out.println(map.get(127) + " " + map.get(128) + " " + map.get(129));
        System.out.println(map.get(255) + " " + map.get(256) + " " + map.get(257));

        map.remove(1);
        map.remove(128);
        map.remove(257);

        System.out.println(map.get(1) + " " + map.get(2) + " " + map.get(3));
        System.out.println(map.get(127) + " " + map.get(128) + " " + map.get(129));
        System.out.println(map.get(255) + " " + map.get(256) + " " + map.get(257));


    }
}
