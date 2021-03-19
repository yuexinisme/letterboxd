package com.example.demo.concurrent;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyRunnable implements Runnable{

    private String name;

    private Condition condition;

    private ReentrantLock lock;

    MyRunnable(String name, Condition condition, ReentrantLock lock) {
        this.name = name;
        this.condition = condition;
        this.lock = lock;
    }

    @SneakyThrows
    @Override
    public void run(){
        lock.lock();
        if (name.equals("MARIA")) {
            System.out.println("s");
        }
        if (name.equals("RIZ")) {
            System.out.println("s");
        }
        if (name.equals("STEVEN")) {
            System.out.println("s");
        }
        System.out.println("name is " + name);

        condition.await();
        System.out.println("name isn't " + name);
        lock.unlock();
    }
}
