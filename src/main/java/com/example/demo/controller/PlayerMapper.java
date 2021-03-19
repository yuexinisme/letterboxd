package com.example.demo.controller;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlayerMapper {

    @Insert("insert into players (name, country, ranking, age, year) values (#{name}, #{country}," +
            "#{ranking}, #{age}, #{year})")
    int add(Player player);

    @Select("select count(*) from players where name=#{name} and year=#{year}")
    int check(String name, Integer year);

    @Select("select min(ranking) as min from players where name=#{name}")
    Integer getMin(String name);

    @Select("select * from players where name=#{name} order by year limit 1")
    Player getOne(String name);

    @Select("select * from players where name = #{name} and year >= 2009 order by year")
    List<Player> getMany(String name);

    @Select("select name from players group by name")
    List<String> getAllNames();
}
