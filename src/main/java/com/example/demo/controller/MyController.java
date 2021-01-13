package com.example.demo.controller;


import com.example.demo.controller.LikesMapper;

import com.opencsv.CSVReader;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
    KafkaConsumer consumer;

    @Autowired
    Collector collector;

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    ElasticsearchOperations op;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping("get")
    @ResponseBody
    @CrossOrigin
    public Long getNum(@RequestParam("name") String name, HttpServletResponse res) {
        res.setHeader("Content-Type", "application/json;charset=utf-8");
        return likesMapper.getNum(name);
    }

    @GetMapping("scan")
    @ResponseBody
    public void scan() throws Exception {
        collector.collectLikes();
    }

    @PostMapping("test")
    @ResponseBody
    public void test(@RequestBody String gay, int age) {
        //PageHelper.startPage(1,3);
        likesMapper.getNum("d");
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
        kafkaTemplate.send("quickstart-events", "demo", msg);
        return "x";
    }

}
