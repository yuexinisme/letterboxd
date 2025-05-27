package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Unsafe;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Component
@WebFilter
@Data
public class TestService {

    public final void s() {

    }

    public static HashMap<String, List<String>> maps = new HashMap<>();

    {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/Users/nick/Desktop/xx.csv"));
        } catch (IOException e) {

        }
        List<Bean> beans = new ArrayList<>();
        // Convert lines to Bean objects
//        for (String line : lines) {
//            try {
//                System.out.println(line);
//                String[] fields = line.split(",");
//                Bean bean = new Bean();
//                bean.setNumber(Integer.parseInt(fields[0]));
//                bean.setName(fields[1]);
//                bean.setYear(Integer.parseInt(fields[2]));
//                bean.setUrl(fields[3]);
//                bean.setTags(fields.length > 4 ? fields[4] : "");
//                beans.add(bean);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        for (Bean bean:beans) {
//            String tags = bean.getTags();
//            if (!StringUtils.isNullOrEmpty(tags)) {
//                String[] split = tags.split("//");
//                for (String s:split) {
//                    List<String> list = maps.getOrDefault(s, new ArrayList<>());
//                    list.add(String.format("<b>" + "%05d", bean.getNumber()) + "." + bean.getName()  + "</b>  <a target=\"_blank\" href=' " + bean.getUrl() + "'>" + bean.getUrl() + "</a>  ->  " + bean.getTags());
//                    maps.put(s, list);
//                }
//            }
//        }
    }

    @Autowired
    TestMapper testMapper;



    public static void main(String[] args) throws Exception {
        String base64 = "ACrL2ZACrpbG0vZmljaGVmaWxtX2dlbl9jZmlsbT0zMjU3MDguaHRtbA";
        byte[] decoded = Base64.getDecoder().decode(base64);

        // 用 ISO_8859_1 保证一一映射，不丢数据
        String raw = new String(decoded, "UTF-8");
        System.out.println(raw);
        // 只提取有效的 URL 部分
        int start = raw.indexOf("/film/");
        if (start != -1) {
            System.out.println("提取结果：" + raw.substring(start));
        } else {
            System.out.println("未找到 /film/ 开头的内容");
        }
    }


}
