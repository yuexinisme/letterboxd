package com.example.demo.concurrent;

//import com.alibaba.dubbo.config.annotation.Service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Service
public interface A {

    String test();
}
