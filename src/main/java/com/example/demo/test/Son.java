package com.example.demo.test;

import com.example.demo.model.Book;
import com.fasterxml.jackson.databind.ObjectReader;

import org.apache.http.HttpRequestInterceptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.*;

public class Son implements Callable<String> {

    {
        System.out.println("son static");
    }

    public Son() {
        System.out.println("son was born");
    }

    public Dad dad;

    static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception{
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
        long l = System.nanoTime();
        Thread.sleep(100);
        System.out.println(System.nanoTime() - l);
        LinkedBlockingQueue queue;
    }

        @Override
        public String call() throws Exception {
            return "xixi";
        }
    }
