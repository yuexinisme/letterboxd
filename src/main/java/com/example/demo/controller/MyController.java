package com.example.demo.controller;


import com.example.demo.controller.LikesMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.List;

/**
 * @author Nick Yuan
 * @date 2020/3/26
 * @mood shitty
 */
@Controller
public class MyController {

   @Autowired
   LikesMapper likesMapper;

    @GetMapping("get")
    @ResponseBody
    public Long getNum(@RequestParam("name") String name, HttpServletResponse res) {
        res.setHeader("Content-Type", "application/json;charset=utf-8");
        return likesMapper.getNum(name);
    }
}
