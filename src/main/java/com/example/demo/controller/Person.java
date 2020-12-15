package com.example.demo.controller;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Person {
    private String name;
    private Integer age;

    public static void main(String[] args) {
        List<Person> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        System.out.println(l1.getClass() == l2.getClass());
    }
}
