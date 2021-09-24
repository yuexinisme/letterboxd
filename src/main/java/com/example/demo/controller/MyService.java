package com.example.demo.controller;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MyService {

    @Transactional
    public void x() {
        System.out.println("");
    }

    public void y() {
        System.out.println("");
    }
}
