package com.example.demo;

//import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
//import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.example.demo.controller.Person;
//import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@MapperScan(value = {"com.example.demo.controller", "com.example.demo.mapper"})

//@EnableDubbo
//@ComponentScan({"com.mifmif.common.regex", "com.example.demo"})
@EnableDiscoveryClient
@EnableConfigServer
public class DemoApplication {

	@Autowired
	 ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

}
