package com.example.demo.bean;

import com.github.pagehelper.Page;
import lombok.Data;

@Data
public class Review {

    private Integer id;

    private String title;

    private Integer number;

    private String director;

    Page p;
}
