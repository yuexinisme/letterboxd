package com.example.demo.concurrent;

//import com.alibaba.dubbo.config.annotation.Service;
import com.example.demo.controller.Person;
import com.example.demo.controller.Player;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope()
@Data
//@Service(version = "1.3", group = "g1")
public class B implements A{


    @Override
    public String test() {
        int n = 3;
        n = n >>> 1;
        System.out.println(n);
        return "yes";
    }

    public static void main(String[] args) {
        "".intern();
    }
}
