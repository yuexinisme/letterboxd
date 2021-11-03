package com.example.demo.test;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Component("guy")
@Scope("prototype")
public class Guy implements FactoryBean<String> {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String getObject() throws Exception {
        return "null";
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    public Guy() {
        System.out.println("guy 1");
    }


    @Autowired
    public Guy(Son son) {
        System.out.println("guy 2");
        ExecutorService p = Executors.newSingleThreadExecutor();
        ArrayBlockingQueue queue;
        p.execute(null);
        ScheduledThreadPoolExecutor e = new ScheduledThreadPoolExecutor(4);
        RLock lock = redissonClient.getLock("");
        lock.lock(1, TimeUnit.DAYS);
        lock.unlock();
        ReentrantLock lock1 = new ReentrantLock();
        lock.lock();
        HashMap map;
    }
}
