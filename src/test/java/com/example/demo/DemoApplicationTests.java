package com.example.demo;

import com.example.demo.controller.LikesMapper;

//import com.github.pagehelper.PageHelper;
import com.example.demo.test.Son;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.PriorityOrdered;
import org.springframework.web.servlet.DispatcherServlet;

import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@DependsOn("son")
class DemoApplicationTests {

private static int MB = 1024*1024;
    static byte[] b1,b2,b3,b4;

    @Autowired
    ApplicationContext context;

    public static void main(String[] args) throws Exception{
        new HashSet().add(3);
        LinkedList ll = new LinkedList();
        ll.push(2);
        ConcurrentHashMap map = new ConcurrentHashMap();
        map.put(1,2);
        ThreadPoolExecutor exe = new ThreadPoolExecutor(5,10,100, TimeUnit.DAYS, new ArrayBlockingQueue(5));
        Future<?> submit = exe.submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        submit.get();
        submit.isDone();


        //System.out.println(Base64.encodeBase64String(UUID.randomUUID().toString().replaceAll("-","").getBytes()));
    }
//
	@Test
	void add() {
        Object son = context.getBean("person");

    }
//
//	public static void main1(String[] args) throws Exception{
//		ReentrantLock lock = new ReentrantLock();
//		lock.lock();
//		lock.unlock();
//		Condition c = lock.newCondition();
//		c.await();
//		c.signalAll();
//		c.signal();
//		DispatcherServlet ds;
//		CopyOnWriteArrayList<String> x;
//		CyclicBarrier cb = new CyclicBarrier(3, new Runnable() {
//			@Override
//			public void run() {
//
//			}
//		});
//		String[] nums = null;
//		Arrays.sort(nums);
//		int[] x1 = null;
//		Arrays.sort(x1);
//		new ArrayList<>().add(3);
//		Collections.sort(null);
//	}
//	public List<List<Integer>> threeSum(int[] nums) {
//		Set<Integer> cache = new HashSet<>();
//		List<List<Integer>> result = new ArrayList<>();
//		if (nums.length < 3) {
//			return result;
//		}
//		int[] newNums = new int[nums.length];
//		int idx = 0;
//		for (int x = 0; x < nums.length; x++) {
//			int num = nums[x];
//			if (!cache.contains(nums)) {
//				newNums[idx++] = num;
//				cache.add(num);
//			}
//		}
//		System.out.println(Arrays.toString(newNums));
//		for (int i = 0; i < idx - 2; i++) {
//			int target = 0 - newNums[i];
//			cache.clear();
//			for (int j = i + 1; j < idx; j++) {
//				int a = newNums[j];
//				int b = target - a;
//				if (cache.contains(b)) {
//					List<Integer> triplet = new ArrayList<>();
//					triplet.add(newNums[i]);
//					triplet.add(a);
//					triplet.add(b);
//					result.add(triplet);
//				} else {
//					cache.add(a);
//				}
//			}
//		}
//		return result;
//	}
//
//	public static void main(String[] args) throws Exception{
//		ReentrantLock l = new ReentrantLock(true);
//		//PageHelper.startPage(1,2);
//		l.lock();
//		l.unlock();
//		Condition c = l.newCondition();
//		c.await();
//		c.signal();
//		c.signalAll();
////		new Thread().isInterrupted();
//		CyclicBarrier cb;
//		CountDownLatch cdl;
//
//		Semaphore sp = new Semaphore(3, true);
//		sp.acquire();
//		sp.acquire(3);
//		sp.release(3);
//		sp.release();
////		ExecutorService p = Executors.newSingleThreadExecutor();
////		ArrayList<Object> objects = null;
////		p.shutdown();
////		objects.toString();
////		Arrays.asList();
////		objects.size();
////		p.execute(new Runnable() {
////			@Override
////			public void run() {
////
////			}
////		});
//		CyclicBarrier x = new CyclicBarrier(4, new Runnable() {
//			@Override
//			public void run() {
//
//			}
//		});
//		x.await();
//		CountDownLatch cc = new CountDownLatch(3);
//		cc.countDown();
//		cc.await();
//		ThreadLocal tl = new ThreadLocal();
//		tl.set(3);
//		tl.get();
//		Executors.newCachedThreadPool();
//		BlockingQueue q;
//		//q.put(2);
//	}
//
//	private void change() {
//		flag = true;
//	}
//
//	public int lengthOfLastWord(String s) {
//		char[] chars = s.toCharArray();
//		boolean started = false;
//		int idx = 0;
//		for (int i = chars.length - 1; i >= 0; i--) {
//			if (chars[i] != ' ' && !started) {
//				started = true;
//				idx = 1;
//			}
//			if (started && chars[i] != ' ') {
//				idx++;
//			}
//			if (started && chars[i] == ' ') {
//				return idx;
//			}
//		}
//		return idx;
//	}
}
