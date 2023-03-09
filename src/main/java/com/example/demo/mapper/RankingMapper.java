package com.example.demo.mapper;

import com.example.demo.bean.Ranking;
import com.example.demo.bean.Review;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RankingMapper {

    @Insert("insert into Ranking(name,num,year,country,birthday) values(#{name},#{num},#{year},#{country},#{birthday})")
    int insert(Ranking r);

    @Select("select min(num) from ranking where name = #{name}")
    Integer getHighestRanking(@Param("name") String name);

    @Select("select min(year) from ranking where name = #{name}")
    Integer getFirstYear(@Param("name") String name);

    @Select("select num from ranking where name = #{name} order by year")
    List<String> getRanks(@Param("name") String name);

    @Select("select max(year) from ranking where name = #{name}")
    Integer getLastYear(@Param("name") String name);

    @Select("select distinct name from ranking")
    List<String> getNames();

    @Select("select * from ranking where name = #{name} limit 1")
    Ranking getOne(@Param("name") String name);

    @Insert("insert into review(title,number,director) values(#{review.title},#{review.number},#{review.director})")
    void saveReview(@Param("review") Review review);
}
