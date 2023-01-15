package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import javax.servlet.annotation.WebFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@Component
@WebFilter
public class TestService {

    @Autowired
    TestMapper testMapper;

    public String getCount() {
        Arrays.asList(null).add(1);
        new HashMap<>().values();
        Unsafe yl;
        return testMapper.getCount();
    }

    public static void main(String[] args) {
        BlockingQueue q;
        System.out.println(1951 + new Random().nextInt(72) + " " + new Random().nextInt(3));
    }
}
