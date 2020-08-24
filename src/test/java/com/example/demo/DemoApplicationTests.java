package com.example.demo;

import com.example.demo.controller.LikesMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
@Autowired
	LikesMapper mapper;
	@Test
	void add() {
		for (int i = 1;i < 23;i++) {
			String url = "https://letterboxd.com/yuexinisme/films/reviews/page/" + i;

		}
	}

}
