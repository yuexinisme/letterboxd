package com.example.demo.concurrent;

import com.example.demo.controller.Person;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
@EnableConfigurationProperties(Person.class)
public class MyRunnable{

}
