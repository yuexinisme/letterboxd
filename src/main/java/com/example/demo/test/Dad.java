package com.example.demo.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Dad {

    public Dad() {
        System.out.println("dad was born");
    }

    public static void main(String[] args) {
        ExecutorService s = Executors.newFixedThreadPool(3);
        s.execute(null);
        s.shutdown();
        new ReentrantLock().lock();
    }
}
