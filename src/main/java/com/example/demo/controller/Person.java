package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.inject.Scope;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
//@Component
@Singleton
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name = "katy";
    private Integer age;

    public static void main(String[] args) throws Exception{
        System.out.println(Person.class.getClassLoader());
        System.out.println(String.class.getClassLoader());
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> f = pool.submit(new Runnable() {
            @Override
            public void run() {

            }
        });

        pool.shutdown();
        pool.shutdownNow();
        ThreadLocal l = new ThreadLocal();
        l.set(2);

        l.get();
        Connection c = DriverManager.getConnection("", "", "");
        Statement statement = c.createStatement();
        PreparedStatement pd = c.prepareStatement("");

    }
}
