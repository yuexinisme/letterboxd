package com.example.demo.mapper;

import com.example.demo.bean.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface RecordMapper {

    @Insert("insert into record (username, followed, time) values (#{username}, #{followed}, #{time})")
    void save(Record record);

    @Select("select count(1) from record where username = #{username} and followed=#{followed} and " +
            "time = #{time}")
    int verify(Record record);


}
