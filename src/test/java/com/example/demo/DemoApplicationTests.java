package com.example.demo;

import com.example.demo.controller.LikesMapper;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class DemoApplicationTests {
@Autowired
	LikesMapper mapper;
	@Test
	void add() {
		ClassPathXmlApplicationContext c = new ClassPathXmlApplicationContext("");
		c.getBean("");
		BeanPostProcessor d;
		InstantiationAwareBeanPostProcessor x;
		new StringBuilder().append(3);
		BeanPostProcessor p;
		InstantiationAwareBeanPostProcessor xx;

	}

	public static void main(String[] args) throws Exception{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		lock.unlock();
		Condition c = lock.newCondition();
		c.await();
		c.signalAll();
		c.signal();
		DispatcherServlet ds;
		CopyOnWriteArrayList<String> x;
		CyclicBarrier cb = new CyclicBarrier(3, new Runnable() {
			@Override
			public void run() {

			}
		});

	}

}
