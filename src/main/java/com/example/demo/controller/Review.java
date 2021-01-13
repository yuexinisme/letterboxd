package com.example.demo.controller;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "review")
@Data
public class Review {
    @Id
    private Integer id;
    @Field
    private String name;
    @Field(type = FieldType.Integer)
    private String year;
    private String url;
    @Field(type = FieldType.Float)
    private Double rating;
    @Field
    private String content;
    @Field
    private String tags;
    private Date date;
}
