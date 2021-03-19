package com.example.demo.controller;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @author Nick Yuan
 * @date 2020/8/24
 * @mood shitty
 */
public interface LikesMapper {

    @Select("select count(movie) c from likes where name=#{name}")
    Long getNum(String name);

    @Insert("insert into likes (name,movie) values(#{name},#{movie})")
    void add(String name, String movie);

    @Select("select count(movie) c from likes where name=#{name} and movie=#{movie}")
    Long check(String name, String movie);

    @Insert("insert into people values('${name}')")
    void x(String name);
}
