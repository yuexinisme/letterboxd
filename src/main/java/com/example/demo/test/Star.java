package com.example.demo.test;

import com.github.pagehelper.Page;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public enum Star {
    GA,
    KATY;

    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("/Users/nickyuan/downloads/1.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(new Son());
        FileInputStream fis = new FileInputStream("/Users/nickyuan/downloads/1.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object o = ois.readObject();
        System.out.println(((Son)o).age);
        ArrayList<Star> list = new ArrayList<>();
        list.toArray();
        Iterator<Star> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
        BeanPostProcessor p;
        Integer.valueOf(1);
    }
}

