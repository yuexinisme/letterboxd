package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Collector implements ApplicationRunner {

    @Autowired
    LikesMapper mapper;

    /**
     *
     * @return
     * @throws Exception
     */
    public Map<String, Integer> collectLikes() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "yuexinisme");
        PreparedStatement statement = conn.prepareStatement("insert into likes (name, movie) values (?,?)");
        PreparedStatement checkState = conn.prepareStatement("select * from likes where id=? and movie=?");
        Map<String, Integer> res = new HashMap<String, Integer>();
        for (int page = 1;; page++) {
            Document document;
            try {
                 document = Jsoup.connect("https://letterboxd.com/yuexinisme/films/reviews/page/" + page)
                        .get();
            } catch (Exception e) {
                continue;
            }

            System.out.println("https://letterboxd.com/yuexinisme/films/reviews/page/" + page);
            //System.out.println(document);
            Elements names = document.getElementsByClass("like-link-target react-component -monotone");
            if (names.size() == 0) {
                break;
            }
            for (Element e:names) {
                //System.out.println(e);
                String url = e.attr("data-likes-page");
                String fullUrl = "https://letterboxd.com" + url;
                System.out.println(fullUrl);
                for (int p = 1;;p++) {
                    Document doc;
                    try {
                         doc = Jsoup.connect(fullUrl + "page/" + p)
                                .get();
                    } catch (Exception eq) {
                        continue;
                    }

                    Elements els = doc.select("h3[class=title-3]");
                    if (els.size() == 0)
                        break;
                    for (Element e1:els) {
                        String name = e1.text();
                        System.out.println(name);
//                        statement.setString(1, name);
//                        statement.setString(2, fullUrl);
                        Long num = mapper.check(name, fullUrl);
                        if (num != null && num == 0) {
                            System.out.println("插入, name:" + name + " url: " + fullUrl);
                            mapper.add(name, fullUrl);
                        } else {
                            System.out.println("重复, name:" + name + " url: " + fullUrl);
                        }

                        if (res.containsKey(name)) {
                            res.put(name, res.get(name) + 1);
                        } else {
                            res.put(name, 1);
                        }
                    }
                }


            }
        }
        System.out.println(res);
        return res;
    }

    public static void main(String[] args) throws Exception {
        new Collector().collectLikes();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("starting collection~~~");
        Date date = null;
        SimpleDateFormat f = new SimpleDateFormat();
        while (true) {
            long l = System.currentTimeMillis();
            date = new Date(l);
            int minutes = date.getMinutes();
//            int seconds = date.getSeconds();
//            if (seconds == 0) {
//                String time = f.format(date);
//                System.out.println("当前时间：" + time);
//            }
            if (minutes == 0) {
                String time = f.format(date);
                System.out.println("开始收集，当前时间：" + time);
                try {
                    collectLikes();
                    String t = f.format(new Date());
                    System.out.println("结束收集，当前时间：" + t);
                } catch (Exception e) {

                }
            }
        }
    }
}
