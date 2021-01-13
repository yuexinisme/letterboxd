package com.example.demo.controller;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ReviewRepo extends ElasticsearchRepository<Review,String> {
}
