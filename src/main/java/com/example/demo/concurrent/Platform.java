package com.example.demo.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Platform {

    public static void start() {

    }
    public static void main(String[] args) throws Exception{
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        ExecutorService e = Executors.newFixedThreadPool(5);
        e.execute(new MyRunnable("RIZ", condition, lock));
        e.execute(new MyRunnable("STEVEN", condition, lock));
        e.execute(new MyRunnable("MARIA", condition, lock));
        System.out.println("wait");
        Thread.sleep(4000);
        lock.lock();
        condition.signalAll();
        lock.unlock();
    }
}
