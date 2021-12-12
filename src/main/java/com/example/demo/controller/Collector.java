package com.example.demo.controller;

import com.example.demo.bean.Record;
import com.example.demo.mapper.RecordMapper;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
@Slf4j
@EnableScheduling
public class Collector implements ApplicationRunner {

    @Autowired
    LikesMapper mapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    private RedisTemplate<String, String> template;

    //@Scheduled(fixedRate=2000)
    public void t() {
        System.out.println("test git " +
                "" +
                "一线xxx");
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
        System.out.println("");
    }

//    public void getLikesRecord() throws Exception {
//        String id = "";
//        while (true) {
//            Document document;
//            String tail = id;
//            try {
//                document = Jsoup.connect("https://letterboxd.com/ajax/activity-pagination/NickOfDaSouf/incoming/" + tail)
//                        .get();
//                log.info("tail: " + tail);
//            } catch (Exception e) {
//                continue;
//            }
//
//            //log.info("https://letterboxd.com/NickOfDaSouf/films/reviews/page/", page);
//            //System.out.println(document);
//            Elements sections = document.getElementsByTag("section");
//            log.info("size: " + sections.size());
//            if (sections.size() == 0) {
//                break;
//            }
//            id = "";
//            for (Element e : sections) {
//                //System.out.println(e);
//                String attr = e.attr("data-activity-id");
//                if (!StringUtils.isNullOrEmpty(attr)) {
//                    id = e.attr("data-activity-id");
//                } else {
//                    continue;
//                }
//                log.info("id:" + id);
//                String text = e.text();
//                log.info("text: " + text);
//                if (!text.contains("liked Nick’s")) {
//                    continue;
//                }
//                Elements as = e.getElementsByTag("a");
//                Element e1 = as.get(0);
//                String username = e1.text().trim();
//                Element e2 = as.get(1);
//                String movie = e2.text().trim();
//                Record record = new Record();
//                record.setMovie(movie);
//                record.setUsername(username);
//                Elements time = e.getElementsByTag("time");
//                String datetime = time.get(0).attr("datetime");
//                log.info("datetime: " + datetime);
//                datetime = datetime.substring(0, 10);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                record.setTime(format.parse(datetime));
//                int exists = recordMapper.verify(record);
//                if (exists > 0) {
//                    continue;
//                }
//                recordMapper.save(record);
//                log.info("插入成功 {}", record);
//            }
//            if (StringUtils.isNullOrEmpty(id)) {
//                throw new RuntimeException("id == null");
//            }
//            id = "?after=" + id;
//            log.info("new id: " + id);
//        }
//        log.info("收集完毕");
//    }

    public void checkFollowers() throws Exception {
        int id = 0;
        while (true) {
            Document document;
            int tail = ++id;
            log.info("tail: " + tail);

                document = Jsoup.connect("https://letterboxd.com/nickofdasouf/following/page/" + tail)
                        .get();


            //log.info("https://letterboxd.com/NickOfDaSouf/films/reviews/page/", page);
            //System.out.println(document);
            Elements sections = document.getElementsByClass("avatar -a40");
            log.info("size: " + sections.size());
            if (sections.size() == 0) {
                break;
            }
            for (Element e : sections) {
                //System.out.println(e);
                Record record = new Record();
                String attr = e.attr("href");
                record.setUsername(attr);
                String followUrl = "https://letterboxd.com/" + attr + "films/diary/";
                log.info("followurl " + followUrl);
                try {
                    document = Jsoup.connect(followUrl)
                            .get();
                } catch (Exception ex) {
                    Thread.sleep(1000);
                    document = Jsoup.connect(followUrl)
                            .get();
                }
                Elements dates = document.getElementsByClass("date");
                log.info("dates:" + dates);
                if (dates.size() == 0) {
                    record.setTime(null);
                } else {
                    log.info("text: " + dates.get(0).text());
                    String mouth = dates.get(0).text().substring(0, 3);
                    String year = dates.get(0).text().substring(4, 8);
                    if (!year.equals("2021")) {
                        record.setTime("year");
                    } else {
                        record.setTime(mouth);
                    }
                }
                int exists = recordMapper.verify(record);
                if (exists > 0) {
                    continue;
                }
                recordMapper.save(record);
                log.info("插入成功 {}", record);
            }
        }
        log.info("收集完毕");
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String command1 = "sh redis.sh";
        try
        {
            Process process = Runtime.getRuntime().exec(command1);
            Scanner kb = new Scanner(process.getInputStream());
            while (kb.hasNext()) {
                log.info(kb.nextLine());
            }
            log.info("redis started");
        } catch (IOException e)
        {
            e.printStackTrace();
            log.error("redis failed");
        }
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
