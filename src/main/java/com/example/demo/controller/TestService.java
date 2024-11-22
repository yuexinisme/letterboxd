package com.example.demo.controller;

import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;

@Component
@WebFilter
public class TestService {

    public static HashMap<String, List<String>> maps = new HashMap<>();

    {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/Users/nick/Desktop/xx.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Bean> beans = new ArrayList<>();
        // Convert lines to Bean objects
        for (String line : lines) {
            try {
                System.out.println(line);
                String[] fields = line.split(",");
                Bean bean = new Bean();
                bean.setNumber(Integer.parseInt(fields[0]));
                bean.setName(fields[1]);
                bean.setYear(Integer.parseInt(fields[2]));
                bean.setUrl(fields[3]);
                bean.setTags(fields.length > 4 ? fields[4] : "");
                beans.add(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Bean bean:beans) {
            String tags = bean.getTags();
            if (!StringUtils.isNullOrEmpty(tags)) {
                String[] split = tags.split("//");
                for (String s:split) {
                    List<String> list = maps.getOrDefault(s, new ArrayList<>());
                    list.add(String.format("<b>" + "%05d", bean.getNumber()) + "." + bean.getName()  + "</b>  <a target=\"_blank\" href=' " + bean.getUrl() + "'>" + bean.getUrl() + "</a>  ->  " + bean.getTags());
                    maps.put(s, list);
                }
            }
        }
    }

    @Autowired
    TestMapper testMapper;

    public String getCount() {
        Arrays.asList(null).add(1);
        new HashMap<>().values();
        Unsafe yl;
        return testMapper.getCount();
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("/Users/nick/Desktop/xx.csv"));
        List<Bean> beans = new ArrayList<>();
        // Convert lines to Bean objects
        for (String line : lines) {
            try {
                System.out.println(line);
                String[] fields = line.split(",");
                Bean bean = new Bean();
                bean.setNumber(Integer.parseInt(fields[0]));
                bean.setName(fields[1]);
                bean.setYear(Integer.parseInt(fields[2]));
                bean.setUrl(fields[3]);
                bean.setTags(fields.length > 4 ? fields[4] : "");
                beans.add(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Bean bean:beans) {
            String tags = bean.getTags();
            if (!StringUtils.isNullOrEmpty(tags)) {
                String[] split = tags.split("//");
                for (String s:split) {
//                    List<String> list = maps.getOrDefault(s, new ArrayList<>());
//                    list.add(bean.getName());
//                    maps.put(s, list);
                }
            }
        }
    }
}
