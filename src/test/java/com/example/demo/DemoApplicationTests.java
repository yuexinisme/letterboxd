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

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//@SpringBootTest
class DemoApplicationTests {
@Autowired
	LikesMapper mapper;

private static Boolean flag = false;

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

	public static void main1(String[] args) throws Exception{
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
		String[] nums = null;
		Arrays.sort(nums);
		int[] x1 = null;
		Arrays.sort(x1);
		new ArrayList<>().add(3);
		Collections.sort(null);
	}
	public List<List<Integer>> threeSum(int[] nums) {
		Set<Integer> cache = new HashSet<>();
		List<List<Integer>> result = new ArrayList<>();
		if (nums.length < 3) {
			return result;
		}
		int[] newNums = new int[nums.length];
		int idx = 0;
		for (int x = 0; x < nums.length; x++) {
			int num = nums[x];
			if (!cache.contains(nums)) {
				newNums[idx++] = num;
				cache.add(num);
			}
		}
		System.out.println(Arrays.toString(newNums));
		for (int i = 0; i < idx - 2; i++) {
			int target = 0 - newNums[i];
			cache.clear();
			for (int j = i + 1; j < idx; j++) {
				int a = newNums[j];
				int b = target - a;
				if (cache.contains(b)) {
					List<Integer> triplet = new ArrayList<>();
					triplet.add(newNums[i]);
					triplet.add(a);
					triplet.add(b);
					result.add(triplet);
				} else {
					cache.add(a);
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(flag);
		new DemoApplicationTests().change();
		System.out.println(flag);
		Arrays.asList(1,2);
		new LinkedList().pop();
	}

	private void change() {
		flag = true;
	}
}
