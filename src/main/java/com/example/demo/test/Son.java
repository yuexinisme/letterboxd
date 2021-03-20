package com.example.demo.test;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Son {

    {
        System.out.println("son static");
    }

    public Son() {
        System.out.println("son was born");
    }

    public Dad dad;

    static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(24);
        pool.execute(new Dad(list, "gaga"));
        pool.execute(new Dad(list, "brad"));
        Thread.sleep(3000);
        System.out.println(list);
        Executors.newFixedThreadPool(3);
    }
}
