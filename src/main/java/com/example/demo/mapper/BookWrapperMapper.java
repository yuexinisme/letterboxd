package com.example.demo.mapper;

import com.example.demo.model.Book;
import org.apache.ibatis.annotations.Select;

public interface BookWrapperMapper extends BookMapper {

    @Select("select count(*) from people")
    Integer count();

    @Select("select 100 as num")
    int insert(Book id);
}
