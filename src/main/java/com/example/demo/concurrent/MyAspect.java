package com.example.demo.concurrent;

import com.example.demo.bean.MyLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class MyAspect {

    @Pointcut("@annotation(com.example.demo.bean.MyLock)")
    public void mole() {}

    @After("mole()")
    public void doAfter() {
        System.out.println("嘻嘻嘻");
    }

    @Before("mole()")
    public void debefore() {
        System.out.println("呵呵呵");
    }

    @Around("mole()")
    public void doaround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around start");
        //用的最多通知的签名
        Signature signature = joinPoint.getSignature();
        MethodSignature msg=(MethodSignature) signature;
        Object target = joinPoint.getTarget();
        //获取注解标注的方法
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        //通过方法获取注解
        MyLock annotation = method.getAnnotation(MyLock.class);
        Object proceed;
        String name = annotation.name();
        System.out.println("name: " + name);
        joinPoint.proceed();
        System.out.println("after proceed");
        System.out.println("name again: " + name);
    }
}
