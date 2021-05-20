package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@EnableScheduling
public class Collector implements ApplicationRunner {

    @Autowired
    LikesMapper mapper;

    @Autowired
    private RedisTemplate<String, String> template;

    //@Scheduled(fixedRate=2000)
    public void t() {
        System.out.println("test一线");
    }

    /**
     * @return
     * @throws Exception
     */
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public Map<String, Integer> collectLikes() throws Exception {
        int count = 0;
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat();
        String time = f.format(date);
        log.info("开始收集，当前时间：" + time);
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "yuexinisme");
        PreparedStatement statement = conn.prepareStatement("insert into likes (name, movie) values (?,?)");
        PreparedStatement checkState = conn.prepareStatement("select * from likes where id=? and movie=?");
        Map<String, Integer> res = new HashMap<String, Integer>();
        for (int page = 1; ; page++) {
            Document document;
            try {
                document = Jsoup.connect("https://letterboxd.com/NickOfDaSouf/films/reviews/page/" + page)
                        .get();
            } catch (Exception e) {
                continue;
            }

            //log.info("https://letterboxd.com/NickOfDaSouf/films/reviews/page/", page);
            //System.out.println(document);
            Elements names = document.getElementsByClass("like-link-target react-component -monotone");
            if (names.size() == 0) {
                break;
            }
            for (Element e : names) {
                //System.out.println(e);
                String url = e.attr("data-likes-page");
                String fullUrl = "https://letterboxd.com" + url;
                //log.info(fullUrl);
                movie:
                for (int p = 1; ; p++) {
                    Document doc;
                    boolean hasNew = false;
                    try {
                        doc = Jsoup.connect(fullUrl + "page/" + p)
                                .get();
                    } catch (Exception eq) {
                        continue;
                    }

                    Elements els = doc.select("h3[class=title-3]");
                    if (els.size() == 0)
                        break;
                    for (Element e1 : els) {
                        int repeats = 0;
                        String name = e1.text();
                        //log.info(name);
//                        statement.setString(1, name);
//                        statement.setString(2, fullUrl);
                        Long num = mapper.check(name, fullUrl);
                        if (num != null && num == 0) {
                            //log.info("插入, name:" + name + " url: " + fullUrl);
                            //删除redis缓存
                            template.delete(name + "_COUNT");
                            mapper.add(name, fullUrl);
                            hasNew = true;
                            count++;
                        } else {
                            //log.info("重复, name:" + name + " url: " + fullUrl);

                            //break movie;

                        }
//
//                        if (res.containsKey(name)) {
//                            res.put(name, res.get(name) + 1);
//                        } else {
//                            res.put(name, 1);
//                        }
                    }
                    if (!hasNew) {
                        break;
                    }
                }


            }
        }
        Date d2 = new Date();
        String t = f.format(d2);
        log.info("结束收集，当前时间：" + t);
        log.info("用时：" + (d2.getTime() - date.getTime()) / 1000 / 60 + "分钟");
        log.info("插入{}条", count);
        return res;
    }

    public static void main(String[] args) throws Exception {
        new Collector().collectLikes();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        log.info("starting collection~~~");
//        collectLikes();
//        Date date = null;
//        SimpleDateFormat f = new SimpleDateFormat();
//        while (true) {
//            long l = System.currentTimeMillis();
//            date = new Date(l);
//            int minutes = date.getMinutes();
//            int hour = date.getHours();
////            int seconds = date.getSeconds();
////            if (seconds == 0) {
////                String time = f.format(date);
////                System.out.println("当前时间：" + time);
////            }
//            if (minutes == 0 && hour % 2 == 0) {
//                String time = f.format(date);
//                log.info("开始收集，当前时间：" + time);
//                try {
//                    collectLikes();
//                    Date d2 = new Date();
//                    String t = f.format(d2);
//                    log.info("结束收集，当前时间：" + t);
//                    log.info("用时：" + (d2.getTime() - date.getTime())/1000/60 + "分钟");
//                } catch (Exception e) {
//
//                }
//            }
//        }
    }

}
