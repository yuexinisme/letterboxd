package com.example.demo.test;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Customer {

    Customer() {
        System.out.println("gy");
    }
}
