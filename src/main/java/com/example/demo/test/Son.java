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

    }
    public Son(int age) {
        this.age = age;
    }

    public Dad dad;

    public int age = 12;

    static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception{
        Executors.newCachedThreadPool();

//        Book book = new Book("book of love");
//        book.setName("book of love");
//        FileOutputStream fos = new FileOutputStream("/Users/nickyuan/Downloads/1.txt");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(book);
//        oos.close();
//        fos.close();
//        FileInputStream fis = new FileInputStream("/Users/nickyuan/Downloads/1.txt");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        Book o = (Book) ois.readObject();
//        System.out.println(o.getName());
//        HttpRequestInterceptor g;
//        String a = "1";
//        String b = "21";
//        String c = a + b;
//        String d = "121";
//        System.out.println(c == d.intern());
//        HashMap m = new HashMap();
//        m.keySet();
//        List<Integer> nums = new ArrayList<>();
//        nums.add(1);
//        nums.add(2);
//        nums.add(2);
//        Executors.newCachedThreadPool();
//        Executors.newSingleThreadExecutor();
//        Executors.newFixedThreadPool(4);
//        nums.stream().collect(Collectors.toMap(Integer->Integer,Integer::intValue, (x1,x2)->x2));

        String x = "\n".replaceAll("\\d+","");
        System.out.println(x);
        Date date = new Date();
        date.getMonth();
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
