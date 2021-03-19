package com.example.demo.controller;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component("player")
@Scope("prototype")
public class Player {

    private Integer id;

    private String name;

    private Integer ranking;

    private String country;

    private Integer age;

    private Integer year;
}
