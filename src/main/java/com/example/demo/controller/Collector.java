package com.example.demo.controller;

import com.example.demo.bean.Record;
import com.example.demo.bean.Review;
import com.example.demo.mapper.RankingMapper;
import com.example.demo.mapper.RecordMapper;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
@EnableScheduling
public class Collector implements ApplicationRunner {

    @Autowired
    LikesMapper mapper;

    @Autowired
    private RankingMapper rankingMapper;

    @Autowired
    RecordMapper recordMapper;
//
//    @Autowired
//    private RedisTemplate<String, String> template;

    //@Scheduled(fixedRate=2000)
    public void t() {
        System.out.println("test git " +
                "" +
                "一线xxx");
    }

    public void batchUnfollow() throws Exception {
        //List<String> list = checkUnfollowers();
        FileInputStream fis = new FileInputStream("/Users/nickyuan/Downloads/fuckers.txt");
        InputStreamReader bis = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(bis);
        String line = null;
        HashMap<String, String> map = new HashMap<>();
        map.put("__csrf", "caabe091cbcfb32f2f79");
        while ((line = br.readLine()) != null) {
            String path = line + "unfollow/";
            doPost(path, map);
            Thread.sleep(1000);
        }
    }

    public String doPost(String url, HashMap<String, String> map) throws Exception {
        log.info("path: {}", url);
        String result = "";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(550000).setConnectTimeout(550000)
                .setConnectionRequestTimeout(550000).setStaleConnectionCheckEnabled(true).build();
        client = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        // client = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);

        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.setHeader("Charset", "utf-8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("cookie", "com.xk72.webparts.csrf=caabe091cbcfb32f2f79; usprivacy=1---; ad_clicker=false; _pw_fingerprint=ec3e17937c5e78c229c39afcabfae329; _pw_audience_categories=%5B%22entertainment%22%2C%22movies%22%5D; playwirePageViews=1; _pbjs_userid_consent_data=3524755945110770; pwUID=64250839803848; __gads=ID=8fe1ff97f8bc74fc:T=1642257414:S=ALNI_MbowVNPXVpwufuY-eeKWG56qYS7rQ; supermodel.user.device.cb1a3e46d8fd69f5e1bf6dea5d06e496=1DdG320Nx2hkbggrGnr9NXMxQMeRXL84sUMMU_nf2LXwhvVVQ0EfEd6x6tUoMNvwf0TZEX_DSqVvp0f14TPmMLJZm8n1LEx65cCslvuKycqu7xFqsy8VWDRLd9A69LzALpNzuERupCIRmgycsnCvadMKV2_a3..j1PAf; com.xk72.webparts.user.CURRENT=1MkSmLQtfDvvXBwUVGnd7.f2z3bJ2LLEOycgzyA9Ui8i5T8DTnXV6k8QO..u8BfAqTEsN2vO951NeQHUGZHbZI_WALhFYOaB1pRI; com.xk72.webparts.user=ad87e7695fb7ec3078aa590e414e1fd9171ce0064e885998c4115ae03d558cf3; letterboxd.signed.in.as=NickOfDaSouf");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            params.add(pair);
        }

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        try {
            response = client.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "utf-8");
                }
            }
        } catch (ClientProtocolException e) {
            throw new RuntimeException("创建连接失败" + e);
        } catch (IOException e) {
            throw new RuntimeException("创建连接失败" + e);
        }
        log.info("res: {}", result);
        if (result.contains("true")) {
            log.info("unfollow成功: " + url);
        } else {
            log.info("unfollow失败: " + url);
        }
        return result;
    }



    /**
     * @return
     * @throws Exception
     */
    //@Scheduled(fixedRate = 1000 * 60 * 60 * 12)
    public Map<String, Integer> collectLikes() throws Exception {
        Map<String, String> cookies = new HashMap<>();
        // 从你的 curl 中提取的关键 cookies
        cookies.put("com.xk72.webparts.csrf", "c5cd3f3752272967d79f");
        cookies.put("useMobileSite", "no");
        cookies.put("cf_clearance", "bnHF72j3mGm9FxM9zGU2wGPhvPamdkkS_XxmlSDBnyQ-1769269143-1.2.1.1-rHqhSZ8hSnax87PILaZPNZ4NMRqVs5Ux5VOX.PWN769ioVA2aOljrsDWAawF26a_Uih3ThFSHsG4kmkWHWLZXNBjPDhXpWflVriDEm8.OHubVXsXgrQ9qjW7GLmQQ40vt3XEx6WcoD805MPs93fuO.n8iZMpHtXs.dtNB9_VQ_GfnAB8Q5G3n7rkf4vV2OJN20ZYyMeu3.lEDGN_SC.FXKPtlm6wHnEYzN6YnBp4wqI");
        cookies.put("usprivacy", "1---");
        cookies.put("ad_clicker", "false");
        cookies.put("_sharedid", "8db39792-b629-4e57-b2a2-4abe3ac12424");
        cookies.put("_sharedid_cst", "zix7LPQsHA%3D%3D");
        cookies.put("_li_dcdm_c", ".letterboxd.com");
        cookies.put("_lc2_fpi", "adb4d668fb2b--01kfrahrtdt7znc68x5qj8hhv0");
        cookies.put("_lc2_fpi_meta", "%7B%22w%22%3A1769269158733%7D");
        cookies.put("_lr_retry_request", "true");
        cookies.put("_lr_env_src_ats", "false");
        cookies.put("panoramaId_expiry", "1769873959593");
        cookies.put("_cc_id", "54aa42c03b517fc3db9b3e58bef39c53");
        cookies.put("panoramaId", "5d5e4ff736c854ada0e0fefb9195185ca02cf8f152cccedf9b949a7489283e53");
        cookies.put("_ga_D3ECBB4D7L", "GS2.2.s1769269142$o1$g1$t1769269160$j42$l0$h0");
        cookies.put("_ga_L0W7RDZXX3", "GS2.1.s1769269156$o1$g0$t1769269160$j56$l0$h0");
        cookies.put("_ga", "GA1.1.252084593.1769269142");
        cookies.put("cto_bundle", "HZivRl9RRkhOZnNaOXFtY1hjVUhLYSUyRkhHTzhETWllWUZaUWlpbm1scEExZDVCVEpVWGZoQldTUW1HeE9sZjczdU9BSGxoeGJqQ3Myc2Rta2poT2N6dyUyRjlaQXdvSEo1b3JBWkw4UVpaT0phMCUyRlBLZ0VEZkkyRVBGQTg2Q3hIaXRLMTJ5cA");
        cookies.put("cto_bidid", "OrfK7F9jSDQlMkIlMkZQcnJ5eHdLSEhKRWwyTEZndmk4cmN3UEtybm1GT1paaUlZQ09kT1NudXdHYmNRNmlKN1lZZldBakhPM1M5TVB6bTd4cSUyRm40R21VbjFQVWJ3QSUzRCUzRA");
        cookies.put("_awl", "2.1769269161.5-8a194f5259c1652641b91e69dea6c97c-6763652d617369612d6561737431-0");
        cookies.put("_lr_sampling_rate", "100");

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
            log.info("page:" + page);
            try {
                document = Jsoup.connect("https://letterboxd.com/NickOfDaSouth/films/reviews/page/" + page)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                        //.header("Accept-Encoding", "gzip, deflate, br")
                        .header("Connection", "keep-alive")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("Cache-Control", "max-age=0")
                        .timeout(30000)
                        //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .get();
            } catch (Exception e) {
                throw e;
            }

            log.info("https://letterboxd.com/NickOfDaSouth/films/reviews/page/", page);
            Elements names = document.getElementsByClass("like-link-target react-component");
            if (names.size() == 0) {
                break;
            }
            for (Element e : names) {
                //System.out.println(e);
                String url = e.attr("data-likes-page");
                String fullUrl = "https://letterboxd.com" + url;
                log.info(fullUrl);
                movie:
                for (int p = 1; ; p++) {
                    System.out.println(fullUrl + "page/" + p);
                    Document doc;
                    boolean hasNew = false;
                    try {
                        Thread.sleep(5000);
                        doc = Jsoup.connect(fullUrl + "page/" + p)
                                .cookies(cookies)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                                .header("Accept-Language", "en-US,en;q=0.9")
                                //.header("Accept-Encoding", "gzip, deflate, br")
                                .header("Connection", "keep-alive")
                                .header("Upgrade-Insecure-Requests", "1")
                                .header("Cache-Control", "max-age=0")
                                .header("Sec-Fetch-Dest", "document")
                                .header("Sec-Fetch-Mode", "navigate")
                                .header("Sec-Fetch-Site", "same-origin")
                                .header("Sec-Fetch-User", "?1")
                                .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"")
                                .header("Sec-Ch-Ua-Mobile", "?0")
                                .header("Sec-Ch-Ua-Platform", "\"macOS\"")
                                .header("Priority", "u=0, i")
                                .referrer("https://letterboxd.com/")
                                .timeout(30000)
                                .ignoreHttpErrors(true)
                                //.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                                .get();
                    } catch (Exception eq) {
                        throw eq;
                    }
                    System.out.println(doc);
                    Elements els = doc.select("h3[class=title-3]");
                    if (els.size() == 0)
                        break;
                    for (Element e1 : els) {
                        int repeats = 0;
                        String name = e1.text();
                        log.info(name);
//                        statement.setString(1, name);
//                        statement.setString(2, fullUrl);
                        Long num = mapper.check(name, fullUrl);
                        if (num != null && num == 0) {
                            log.info("插入, name:" + name + " url: " + fullUrl);
                            //删除redis缓存
                            //template.delete(name + "_COUNT");
                            mapper.add(name, fullUrl);
                            hasNew = true;
                            count++;
                        } else {
                            log.info("重复, name:" + name + " url: " + fullUrl);

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
        System.out.println();
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

                document = Jsoup.connect("https://letterboxd.com/nickofdasouth/following/page/" + tail)
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
                String followUrl = "https://letterboxd.com" + attr + "films/diary/";
                log.info("followurl " + followUrl);
                try {
                    document = Jsoup.connect(followUrl)
                            .get();
                } catch (Exception ex) {
                    Thread.sleep(1000);
                    document = Jsoup.connect(followUrl)
                            .get();
                }
                Elements dates = document.getElementsByClass("month");
                log.info("dates:" + dates);
                if (dates.size() == 0) {
                    record.setTime(null);
                } else {
                    log.info("text: " + dates.get(0).text());
                    String text = dates.get(0).attr("href");
                    String[] split = text.split("/");
                    String mouth = split[split.length - 1];
                    String year = split[split.length - 2];
                    if (!year.equals("2025")) {
                        record.setTime(year);
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


    static int id1 = 0, id2 = 0;
    static List<String> followings = new ArrayList<>();
    static List<String> followers = new ArrayList<>();

    public List<String> checkUnfollowers() throws Exception {
        while (true) {
            Document document = null;
            int tail = id1++;
            log.info("tail: " + tail);

            int idx = 0;
            while (idx < 10) {
                try {
                    document = Jsoup.connect("https://letterboxd.com/nickofdasouth/following/page/" + tail).timeout(100000).get();

                } catch (Exception e) {
                    idx++;
                }
                break;
            }
            if (idx == 10) {
                continue;
            }


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
                followings.add(attr);
            }
        }
        log.info("out");
        while (true) {
            Document document = null;
            int tail = id2++;
            log.info("tail: " + tail);

            int idx = 0;
            while (idx < 10) {
                try {
                    document = Jsoup.connect("https://letterboxd.com/nickofdasouth/followers/page/" + tail)
                            .timeout(100000).get();

                } catch (Exception e) {
                    idx++;
                }
                break;
            }
            if (idx == 10) {
                continue;
            }


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
                followers.add(attr);
            }
        }
        followers.remove("/tomslotter/");
        followings.removeAll(followers);
        Collections.reverse(followings);
        System.out.println("followings:::");
        System.out.println(followings);
        return followings;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
//        String command1 = "sh redis.sh";
//        try
//        {
//            Process process = Runtime.getRuntime().exec(command1);
//            Scanner kb = new Scanner(process.getInputStream());
//            while (kb.hasNext()) {
//                log.info(kb.nextLine());
//            }
//            log.info("redis started");
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//            log.error("redis failed");
//        }
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
