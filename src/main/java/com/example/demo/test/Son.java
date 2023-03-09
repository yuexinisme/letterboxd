package com.example.demo.test;

import com.example.demo.model.Book;
import com.fasterxml.jackson.databind.ObjectReader;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpRequestInterceptor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.SelectionKey;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component("son")
@Scope("prototype")
public class Son implements Externalizable {

    {
        System.out.println("son static");
    }

    String s = new String("");

    public Son() {
        System.out.println("son was born");
//        ExecutorService pool = Executors.newCachedThreadPool();
//        pool.execute(null);
//        Future<?> submit = pool.submit(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        pool.shutdownNow();
//        pool.shutdown();
        SelectionKey key;
        char[] chars = "你好".toCharArray();
        for (int i = 0; i < chars.length; i++) {
            System.out.println(chars[i]);
        }
        new Thread().start();

    }
    public Son(int age) {
        this.age = age;
    }

    public Dad dad;

    public int age = 12;

    static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception{
new Son();
    }



    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(1);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        age = in.read();
    }


    public Class<?> getObjectType() {
        return null;
    }
}
