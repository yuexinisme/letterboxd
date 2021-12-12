package com.example.demo.controller;

import com.example.demo.entity.Likes;

import java.util.List;

public interface TestMapper {

    String getCount();

    List<Likes> getLikes();
}
