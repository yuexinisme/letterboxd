package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestService {

    @Autowired
    TestMapper testMapper;

    public String getCount() {
        return testMapper.getCount();
    }
}
