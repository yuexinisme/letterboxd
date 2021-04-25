package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeAdvisor {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();
    @Pointcut("execution(* com.example.demo.controller.MyController.*(..))")
    public void mock(){}

    @Before("mock()")
    public void before(){
        long beforeTime = System.currentTimeMillis();
        LOCAL.set(beforeTime);
    }

    @After("mock()")
    public void After(JoinPoint point){
        long afterTime = System.currentTimeMillis();
        Long beforeTime = LOCAL.get();
        log.info("方法 {} 用时：{} 毫秒", point.getSignature().getName(), afterTime - beforeTime);
    }
}
