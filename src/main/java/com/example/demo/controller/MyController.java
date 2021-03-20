package com.example.demo.controller;


import com.example.demo.controller.LikesMapper;

//import com.github.pagehelper.PageHelper;


import com.github.pagehelper.PageHelper;
import com.opencsv.CSVReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * @author Nick Yuan
 * @date 2020/3/26
 * @mood shitty
 */
@Controller
public class MyController {

    @Autowired
    LikesMapper likesMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    KafkaConsumer consumer;

    @Autowired
    Collector collector;

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    ElasticsearchOperations op;

    @Autowired
    PlayerMapper playerMapper;



    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping("get")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Long getNum(@RequestParam("name") String name, HttpServletResponse res) {
        res.setHeader("Content-Type", "application/json;charset=utf-8");
        likesMapper.getNum(name);
        return likesMapper.getNum(name);

    }

    @GetMapping("scan")
    @ResponseBody
    public void scan() throws Exception {
        int x = 5 & 3;
        collector.collectLikes();
    }

    @GetMapping("test")
    //@ResponseBody
    public String test(ModelAndView mv) {
        //PageHelper.startPage(1,3);
        likesMapper.getNum("name");
        return "idx";
    }

    @GetMapping("demo")
    @ResponseBody
    //@Transactional
    public Object demo(@RequestParam String name) {

        return "";

    }

    private void add(String name) {
        likesMapper.x(name);
        Integer[] ints = {3,4};
        Arrays.sort(ints);
    }

    @GetMapping("es")
    @ResponseBody
    public String es() throws Exception {
        try {
            int id = 0;
            FileInputStream fis = new FileInputStream("/Users/nickyuan/Downloads/reviews.csv");
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            CSVReader reader = new CSVReader(isr);
            for (String[] values; (values = reader.readNext()) != null; ) {
                if (values[0].startsWith("Date")) {
                    continue;
                }
                Review review = new Review();
                review.setName(values[1]);
                review.setUrl(values[3]);
                String s = values[4];
                double v = Double.parseDouble(s);
                review.setRating(v);
                review.setContent(values[6]);
                review.setTags(values[7]);
                String value = values[8];
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = f.parse(value);
                review.setDate(parse);
                review.setId(++id);
                System.out.println(review);
                reviewRepo.save(review);
                System.out.println(review.getName() + "保存！");

            }

            reader.close();
            isr.close();
            fis.close();
        } catch (Exception e) {

        }
        return "done!";
    }

    @GetMapping("search")
    @ResponseBody
    public List<Review> search(@RequestParam String term, @RequestParam Boolean isDesc) throws Exception {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content", term);
        NativeSearchQueryBuilder b = new NativeSearchQueryBuilder();
        b.withQuery(matchQueryBuilder);
        b.withSort(SortBuilders.fieldSort("rating").order(isDesc ? SortOrder.DESC : SortOrder.ASC));
        b.withSort(SortBuilders.fieldSort("id").order(isDesc ? SortOrder.DESC : SortOrder.ASC));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em>");//设置前缀
        highlightBuilder.postTags("</em>");//设置后缀
        highlightBuilder.field("content");//设置高亮字段
        highlightBuilder.fragmentSize(1);
        b.withHighlightBuilder(highlightBuilder);
        //b.addAggregation(AggregationBuilders.terms("grade").field("rating"));
        SearchHits<Review> search = op.search(b.build(), Review.class);
//        Aggregations aggregations = search.getAggregations();
//        //Map<String, Aggregation> aggMap = aggregations.asMap();
//        ParsedDoubleTerms grade = aggregations.get("grade");
//        Iterator<? extends Terms.Bucket> iterator1 = grade.getBuckets().iterator();
//        while (iterator1.hasNext()) {
//            Terms.Bucket buck = iterator1.next();
//            Double team = (Double) buck.getKey();
//            long count = buck.getDocCount();
//            System.out.println("team: " + team);
//            System.out.println("count: " + count);
//        }
        Iterator<SearchHit<Review>> iterator = search.stream().iterator();
        List<Review> reviews = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            SearchHit<Review> next = iterator.next();
            Review r = next.getContent();
            Map<String, List<String>> highlightFields = next.getHighlightFields();
            List<String> content = highlightFields.get("content");
            System.out.println("content:");
            System.out.println(content);
            String c = r.getContent();
            for (String l : content) {
                String replace = l.replace("<em>", "").replace("</em>", "");
                c = c.replace(replace, l);
            }
            r.setContent(c);
            reviews.add(r);
        }
        return reviews;
    }

    @GetMapping("send")
    @ResponseBody
    public String get(@RequestParam String msg) {
        kafkaTemplate.send("kaka", "demo", msg);
        return "x";
    }

    @GetMapping("collect")
    @ResponseBody
    public String collectRanking() throws Exception{
        Document document;
        for (int year = 2009; year < 2022; year++) {
            //determine last monday
            DateTime today = new DateTime(year + "-12-31");
            DateTime sameDayLastWeek = today.minusWeeks(1);
            DateTime mondayLastWeek = sameDayLastWeek.withDayOfWeek(DateTimeConstants.MONDAY);
            if (mondayLastWeek.plusWeeks(1).getYear() == year) {
                mondayLastWeek = mondayLastWeek.plusWeeks(1);
            }
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            if (year == 2021) {
                mondayLastWeek = new DateTime("2021-02-01");
            }
            String s = f.format(mondayLastWeek.toDate());
            try {
                document = Jsoup.connect("https://www.atptour.com/en/rankings/singles?rankDate=" + s + "&rankRange=1-5000")
                        .userAgent("Chrome")
                        .get();
            } catch (Exception e) {
                System.out.println("year " + year + " is f**ked");
                e.printStackTrace();
                return "fffff";
            }
            Elements trs = document.getElementsByTag("tr");
            int ranking = 0;
            for (Element tr:trs) {
                Elements r = tr.getElementsByClass("rank-cell");
                if (r == null || r.size() == 0) {
                    continue;
                } else if (ranking > 999) {
                    break;
                }
                if (r.text() == null) {
                    System.out.println("xxx");
                    System.out.println(r);
                }
                String rk = r.text().trim().replace("T", "");
                System.out.println(rk);
                ranking = Integer.parseInt(rk);
                Elements n = tr.getElementsByClass("player-cell");
                String name = n.text().trim();
                //check
                int check = playerMapper.check(name, year);
                if (check > 0) {
                    System.out.println("deplicate: " + name + "-" + year + "-" + ranking);
                    continue;
                }
                Elements a = tr.getElementsByClass("age-cell");
                int age = -1;
                String country = null;
                try {
                    age = Integer.parseInt(a.text().trim());
                    Elements c = tr.getElementsByClass("country-cell");
                    Element first = c.first();
                    Elements img = first.getElementsByTag("img");
                    Element f1 = img.first();
                    Attributes attributes = f1.attributes();
                    country = attributes.get("alt").trim();
                } catch (Exception e) {

                }
                Player p = new Player();
                p.setAge(age);
                p.setCountry(country);
                p.setName(name);
                p.setRanking(ranking);
                p.setYear(year);
                playerMapper.add(p);
                System.out.println("inserted: " + name + "-" + year + "-" + ranking);
            }
        }
        return "okay";
    }

    @GetMapping("write")
    @ResponseBody
    public String write() throws Exception {
        String prefix = "/Users/nickyuan/Downloads/";
        FileInputStream fis = new FileInputStream(prefix + "ATP.ini");
        InputStreamReader reader = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        PrintWriter pw = new PrintWriter(prefix + "1.ini");
        String name = null;
        String year = null;
        List<String> added = new ArrayList<>();
        Integer smallest = null;
        boolean skip = false;
        boolean keep = false;
        Integer first = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("Name")) {
                name = line.replaceAll("Name\\s+=\\s+", "");
                System.out.println("得到名字 " + name);
                if ("Fabio Fognini".equals(name)) {
                    System.out.println("x");
                }
                smallest = playerMapper.getMin(name);
                if (smallest == null || smallest < 1) {
                    skip = true;
                } else {
                    skip = false;
                }
                added.add(name);
            }
            if (line.startsWith("BestRank") && !skip) {
                pw.write("BestRank\t\t=\t" + smallest + "\n");
                continue;
            }
            if (line.startsWith("FirstYear") && name != null && !skip) {
                Player p = playerMapper.getOne(name);
                int born = p.getYear() - p.getAge();
                first = Integer.parseInt(line.replaceAll("FirstYear\\s+=\\s+", ""));
                if (2010 - born > 40) {
                    pw.write(line + "\n");
                    continue;
                }
                if (first < 2009) {
                    line = line.replace(String.valueOf(first), "2009");
                    first = 2009;
                }
            }
            if (line.startsWith("RankPerYear") && !skip) {
                List<Player> players = playerMapper.getMany(name);
                StringBuilder sb = new StringBuilder();
                Integer y = null;
                Integer ranking = null;
                Map<Integer, Integer> map = players.stream().collect(Collectors.toMap(Player::getYear, Player::getRanking));
                Set<Integer> years = map.keySet();
                Iterator<Integer> iterator = years.iterator();
                int max = -1;
                while (iterator.hasNext()) {
                    Integer x = iterator.next();
                    if (max < x) {
                        max = x;
                    }
                }
                int last = -1;
                for (int i = first; i <= max; i++) {
                    Integer r = map.getOrDefault(i, -1);
                    if (i == first) {
                        sb.append(r);
                    } else {
                        sb.append(", " + r);
                    }
                    if (i == max) {
                        last = r;
                    }
                }
                if (max == 2021) {
                    for (int j = 2022; j < 2026; j++) {
                        sb.append(", " + last);
                    }
                }
                pw.write("RankPerYear\t\t=\t" + sb.toString() + "\n");
                continue;
            }
            if (line.startsWith("SelfEsteem") || line.startsWith("Motivation")) {
                if (!skip && smallest < 11) {
                    int val = 100 - smallest;
                    line = line.replaceAll("\\d+", String.valueOf(val));
                }
            }
            pw.write(line + "\n");
        }
        pw.flush();
        pw.close();
        br.close();
        reader.close();
        fis.close();
        List<String> allNames = playerMapper.getAllNames();
        System.out.println("处理：" + added.size());
        allNames.removeAll(added);
        allNames = allNames.stream().filter(p -> {
            Integer min = playerMapper.getMin(p);
            return min != null && min < 100;
        }).collect(Collectors.toList());
        for (String s:allNames) {
            System.out.println("name: " + s);
            System.out.println("highest: " + playerMapper.getMin(s));
        }
        return "done";
    }

    @GetMapping("marker")
    public ModelAndView marker(Map<String, Object> dataModel, ModelAndView mv) {
        System.out.println("xxxxx");
        dataModel.put("name", "gaga");
        mv.setViewName("abc");
        mv.addObject("name", "katay");
        return mv;
    }
}
