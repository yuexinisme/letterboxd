package com.example.demo.controller;


//import com.github.pagehelper.PageHelper;

//import com.alibaba.dubbo.config.annotation.Reference;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.concurrent.HttpUtils;
import com.example.demo.concurrent.MyThing;
import com.example.demo.mapper.RankingMapper;
import com.mysql.cj.util.StringUtils;
import jodd.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;


import org.apache.http.client.utils.HttpClientUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
        import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
        import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
        import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Nick Yuan
 * @date 2020/3/26
 * @mood shitty
 */
@Controller
@Slf4j
public class MyController {

    @Autowired
    LikesMapper likesMapper;

    @Autowired
    MyAss myAss;

    com.example.t.controller.MyDick myDick;

    @Autowired
    private RedisTemplate<String, String> template;


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
    ApplicationContext context;

    @Autowired
    TestService testService;

    @Autowired
    private RankingMapper rankingMapper;

    @Value("${tennis.wta}")
    private String wtaUrl;







//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping(value = "get", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    @Transactional

    public Long getNum(@RequestParam("name") String name, HttpServletResponse res) {
        Long count;
        String s = null;
        s = template.opsForValue().get(name + "_COUNT");
        if (StringUtils.isNullOrEmpty(s)) {
            log.info("query database: {}", name);
            count = likesMapper.getNum(name);
            template.opsForValue().set(name + "_COUNT", count.toString(), 1, TimeUnit.DAYS);
        } else {
            log.info("hit redis: {}->{}", name, s);
            return Long.valueOf(s);
        }
        return count;

    }

    @GetMapping("scan")
    @ResponseBody
    public void scan() throws Exception {
        int x = 5 & 3;
        collector.collectLikes();
    }
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    @ResponseBody
//    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
//        // 从SecurityUtils里边创建一个 subject
//        Subject subject = SecurityUtils.getSubject();
//        // 在认证提交前准备 token（令牌）
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        // 执行认证登陆
//        try {
//            subject.login(token);
//        } catch (UnknownAccountException uae) {
//            return "未知账户";
//        } catch (IncorrectCredentialsException ice) {
//            return "密码不正确";
//        } catch (LockedAccountException lae) {
//            return "账户已锁定";
//        } catch (ExcessiveAttemptsException eae) {
//            return "用户名或密码错误次数过多";
//        } catch (AuthenticationException ae) {
//            return "用户名或密码不正确！";
//        }
//        if (subject.isAuthenticated()) {
//            return "登录成功";
//        } else {
//            token.clear();
//            return "登录失败";
//        }
//    }

    @GetMapping(value = "test")
    @ResponseBody
    public String test(@RequestParam String msg_signature, @RequestParam String nonce,
                       @RequestParam String timestamp, @RequestParam(required = false) String echostr, @RequestBody(required = false) String xml) throws AesException {
        String sToken = "QDG6eK";
        String sCorpID = "ww92e9e21977a625b8";
        String sEncodingAESKey = "OTNjZTdiYmM3MDA1NDc2ZmEwNmY3YWVlZmQ0OTZmZTg";
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String s = wxcpt.VerifyURL(msg_signature, nonce, timestamp, echostr);
        return s;

    }

    @PostMapping(value = "test")
    @ResponseBody
    public String test1(@RequestParam String msg_signature, @RequestParam String nonce,
                       @RequestParam String timestamp, @RequestParam(required = false) String echostr, @RequestBody(required = false) String xml) throws AesException, JAXBException, UnsupportedEncodingException, XMLStreamException {
        String sToken = "QDG6eK";
        String sCorpID = "ww92e9e21977a625b8";
        String sEncodingAESKey = "OTNjZTdiYmM3MDA1NDc2ZmEwNmY3YWVlZmQ0OTZmZTg";
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        log.info("xml: {}", JSON.toJSONString(xml));
        if (xml != null) {
            String s = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, xml);
            log.info("s : {}", s);
            xml o = xmlToObject(s);
            log.info("xml after: {}", JSON.toJSONString(o));
            if (o.getSuiteTicket() != null) {
                log.info("ticket: {}", o.getSuiteTicket());
                template.boundValueOps("ticket").set(o.getSuiteTicket());
            }
            //template.boundValueOps("ticket").set(o.getSuiteTicket());
            return "success";
        }
        String s = wxcpt.VerifyURL(msg_signature, nonce, timestamp, echostr);
        return s;

    }
    public static xml xmlToObject(String xmlString) throws XMLStreamException, UnsupportedEncodingException, JAXBException {
        StringReader sr = new StringReader(xmlString);
        JAXBContext jaxbContext = JAXBContext.newInstance(xml.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        xml response = (xml) unmarshaller.unmarshal(sr);
        return response;
    }

    private void add(String name) {
        likesMapper.x(name);
        Integer[] ints = {3,4};
        Arrays.sort(ints);
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
        //kafkaTemplate.send("kaka", "demo", msg);
        return "x";
    }

    //pull rankings from ATP website
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

    //generate ATP.ini
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
            } else if (line.startsWith("RankPerYear") && skip) {
               String ranks = line.replaceAll("RankPerYear\t\t=\t", "");
                String[] split = ranks.split(", ");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    Integer rank = Integer.valueOf(s);
                    if (i == 0) {
                        sb.append(rank);
                    } else {
                        sb.append(", " + rank);
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

    //~~~
    //pull rankings from WTA website
    @GetMapping("collectWomen")
    @ResponseBody
    public String collectRankingWomen() throws Exception{
        for (int i = 2000; i < 2022; i++) {
            String date = i == 2021?"2021-05-14":i + "-12-31";
            for (int j = 0; j < 10; j++) {
                log.info("date: {}", date);
                String url = wtaUrl + date + "&page=" + j;
                log.info("url: {}", url);
                String s = HttpUtils.doGet(url);
                JSONArray array = JSONArray.parseArray(s);
                Iterator<Object> iterator = array.iterator();
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    JSONObject jo = JSONObject.parseObject(next.toString());
                    Integer ranking = (Integer) jo.get("ranking");
                    Ranking r = new Ranking();
                    r.setYear(i);
                    r.setNum(ranking);
                    String player =jo.get("player").toString();
                    JSONObject p = JSONObject.parseObject(player);
                    String fullName = p.get("fullName").toString();
                    r.setName(fullName);
                    String countryCode = p.get("countryCode").toString();
                    r.setCountry(countryCode);
                    String dateOfBirth = p.get("dateOfBirth").toString();
                    r.setBirthday(dateOfBirth);
                    rankingMapper.insert(r);
                    log.info("插入：{}", JSON.toJSONString(r));
                }
            }

        }
        return "okay";
    }

    //generate WTA.ini
    @GetMapping("writeWomen")
    @ResponseBody
    public String writeWomen() throws Exception {
        String prefix = "/Users/nickyuan/Downloads/";
        FileInputStream fis = new FileInputStream(prefix + "WTA.ini");
        InputStreamReader reader = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        PrintWriter pw = new PrintWriter(prefix + "2.ini");
        String name = null;
        String year = null;
        List<String> added = new ArrayList<>();
        Integer smallest = null;
        boolean skip = false;
        boolean keep = false;
        Integer first = null;
        List<String> names = rankingMapper.getNames();
        int count = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("Name")) {
                name = line.replaceAll("Name\\s+=\\s+", "");
                count++;
                if (!names.contains(name)) {
                    skip = true;
                    pw.write(line + "\n");
                    continue;
                } else {
                    skip = false;
                }
                names.remove(name);
                System.out.println("得到名字 " + name);
                //获取best ranking
                smallest = rankingMapper.getHighestRanking(name);
                if (smallest == 0) {
                    throw new RuntimeException("smallest == 0");
                }
            }
            if (line.startsWith("BestRank") && !skip) {
                pw.write("BestRank\t\t=\t" + smallest + "\n");
                continue;
            }
            if (line.startsWith("FirstYear") && name != null && !skip) {
                first = rankingMapper.getFirstYear(name);
                pw.write("FirstYear\t\t=\t" + first + "\n");
                continue;
            }
            if (line.startsWith("RankPerYear") && !skip) {
                List<String> ranks = rankingMapper.getRanks(name);
                String join = String.join(", ", ranks);
                pw.write("RankPerYear\t\t=\t" + join + "\n");
                continue;
            }
            if ((line.startsWith("SelfEsteem") || line.startsWith("Motivation")) && !skip) {
                if (smallest < 11) {
                    int val = 100 - smallest;
                    line = line.replaceAll("\\d+", String.valueOf(val));
                }
            }
            pw.write(line + "\n");

        }
        count += 100;
        for (String n:names) {
            Ranking one = rankingMapper.getOne(n);
            Integer highestRanking = rankingMapper.getHighestRanking(n);
            String birthday = one.getBirthday();
            SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
            Date d = f1.parse(birthday);
            SimpleDateFormat f2 = new SimpleDateFormat("dd/MM/yyyy");
            String date = f2.format(d);
            pw.write("\n");
            pw.write("[Player");
            pw.write(count++);
            pw.write("]\n");
            pw.write("Name\t\t=\t" + n + "\n");
            pw.write("Country\t\t=\t" + one.getCountry() + "\n");
            pw.write("BestRank\t\t=\t" + highestRanking + "\n");
            pw.write("Style\t\t=\t" + "PowerBaseliner" + "\n");
            pw.write("Birthdate\t\t=\t" + date + "\n");
            pw.write("Body\t\t\t=\t185 85" + "\n");
            Integer firstYear = rankingMapper.getFirstYear(n);
            pw.write("FirstYear\t\t=\t" + firstYear + "\n");
            List<String> ranks = rankingMapper.getRanks(n);
            String join = String.join(", ", ranks);
            pw.write("RankPerYear\t\t=\t" + join + "\n");
            pw.write("SingleDouble\t\t=\t0.25\n" +
                    "Forehand_Power\t\t=\t46\n" +
                    "Forehand_Consistency\t=\t18\n" +
                    "Forehand_Precision\t=\t45\n" +
                    "Backhand_Power\t\t=\t58\n" +
                    "Backhand_Consistency\t=\t47\n" +
                    "Backhand_Precision\t=\t33\n" +
                    "Service_Power\t\t=\t19\n" +
                    "Service_Consistency\t=\t35\n" +
                    "Service_Precision\t=\t33\n" +
                    "Return\t\t\t=\t53\n" +
                    "Lob\t\t\t=\t15\n" +
                    "Passing\t\t\t=\t80\n" +
                    "Dropshot\t\t=\t0\n" +
                    "Counter\t\t\t=\t4\n" +
                    "ForehandVolley\t\t=\t32\n" +
                    "BackhandVolley\t\t=\t32\n" +
                    "Smash\t\t\t=\t30\n" +
                    "NetPresence\t\t=\t32\n" +
                    "Topspin\t\t\t=\t25\n" +
                    "Speed\t\t\t=\t25\n" +
                    "Stamina\t\t\t=\t33\n" +
                    "Tonicity\t\t=\t54\n" +
                    "Reflexes\t\t=\t0\n" +
                    "Strength\t\t=\t1\n" +
                    "Concentration\t\t=\t45\n" +
                    "Focus\t\t\t=\t34\n" +
                    "ColdBlood\t\t=\t29\n" +
                    "Constancy\t\t=\t38\n" +
                    "Tactic\t\t\t=\t37\n" +
                    "Positioning\t\t=\t0\n");
            if (highestRanking < 11) {
                int val = 100 - highestRanking;
                pw.write("SelfEsteem\t\t=\t" + val + "\n");
                pw.write("Motivation\t\t=\t" + val + "\n");
            } else {
                pw.write("SelfEsteem\t\t=\t" + "50" + "\n");
                pw.write("Motivation\t\t=\t" + "50" + "\n");
            }
            pw.write("DoubleSpirit\t\t=\t52\n" +
                    "Hand\t\t\t=\tRight 2HBH \n");
        }
        pw.flush();
        pw.close();
        br.close();
        reader.close();
        fis.close();
        return "done";
    }
}
