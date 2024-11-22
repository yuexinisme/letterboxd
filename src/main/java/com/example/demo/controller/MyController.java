package com.example.demo.controller;


//import com.github.pagehelper.PageHelper;

//import com.alibaba.dubbo.config.annotation.Reference;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.bean.*;
import com.example.demo.concurrent.HttpUtils;
import com.example.demo.mapper.RankingMapper;
import com.example.demo.test.Dad;
import com.example.demo.test.Son;
import com.mysql.cj.util.StringUtils;
import jodd.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.common.util.CollectionUtils;
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
import org.springframework.beans.factory.annotation.Qualifier;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
    private RedisTemplate<String, String> template;


    @Autowired
    @Qualifier
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

    @Resource(name = "son")
    private Son son;

    @Autowired
    private MyService myService;

    @Autowired
    private Dad dad;






//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping(value = "get", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Long getNum(@RequestParam("name") String name, HttpServletResponse res) throws Exception{
        return likesMapper.getNum(name);
    }

    @GetMapping(value = "get10", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public String get10(@RequestParam("name") String name, HttpServletResponse res) throws Exception{
        return likesMapper.get10(name);
    }

    @GetMapping(value = "get1", produces = "application/json")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Long getNum1(@RequestParam("name") String name, HttpServletResponse res) throws Exception{
        HttpClient hc = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://124.220.7.103:8090/get?name=" + URLEncoder.encode(name));
        HttpResponse response=hc.execute(get);
        int code=response.getStatusLine().getStatusCode();
        if(code==200){
            HttpEntity result=response.getEntity();

            //转换成string类型
            String str= EntityUtils.toString(result);
            return Long.valueOf(str);
        }
        return 0L;
    }

    @GetMapping("scan")
    @ResponseBody
    public void scan() throws Exception {
        collector.collectLikes();
    }


    public void t() {
        System.out.println("t main");
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

    List objectList = new ArrayList();

    @GetMapping("/oom")
    public String createOOM() {
        while (true) {
            objectList.add(new byte[1024*1024*10000]); // Constantly adding new objects to list
        }
    }

    @GetMapping(value = "test")
    @ResponseBody
    public String test() throws Exception {
        return JSONObject.toJSONString(collector.checkUnfollowers());
    }

    @GetMapping(value = "test1")
    @ResponseBody
    public String test1() throws Exception {
        collector.checkFollowers();
        return "2";
    }



    @Transactional
    private void add(String name) {
        System.out.println("");
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
        boolean alter = false;
        Integer first = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("Name")) {
                name = line.replaceAll("Name\\s+=\\s+", "");
                System.out.println("得到名字 " + name);
                if ("Fabio Fognini".equals(name)) {
                    System.out.println("x");
                }
                Player one = playerMapper.getOne(name);
                if (one == null) {
                    alter = true;
                } else {
                    alter = false;
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
            if (line.startsWith("BestRank") && skip) {
                int best = smallest == null?0:smallest;
                best += 500;
                pw.write("BestRank\t\t=\t" + best + "\n");
                continue;
            }
            if (line.startsWith("FirstYear") && skip) {
                pw.write("FirstYear\t\t=\t" + 1980 + "\n");
                continue;
            }
            if (line.startsWith("FirstYear") && name != null && !skip) {
                if (alter) {
                    first = 1990;
                    continue;
                }
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
                    int rank = (int)(double)Double.valueOf(s);
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

    @ResponseBody
    @GetMapping("csv")
    public String csv(@RequestParam String tag) throws Exception {
        HashMap<String, List<String>> maps = TestService.maps;
        TreeSet<String> set = new TreeSet<>();
        Set<String> keys = maps.keySet();
        for (String k:keys) {
            if (k.contains(tag)) {
                set.addAll(maps.get(k));
            }
        }
        if (set.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (String l:set) {
                sb.insert(0, l.replaceAll(tag, "<span style=\"color:red;\">" + tag + "</span>"));
                sb.insert(0, "<br><br>");
            }
            return sb.toString();
        }
        return "";
    }

    @ResponseBody
    @GetMapping("csvAll")
    public String csvAll() throws Exception {
        HashMap<String, List<String>> maps = TestService.maps;
        TreeSet<String> set = new TreeSet<>();
        Set<String> keys = maps.keySet();
        for (String k:keys) {
            for (String word:WebCrawler.BadWords) {
                if (k.contains(word) && (k.indexOf(word) == 0 || k.charAt(k.indexOf(word) - 1) == ' ' || k.charAt(k.indexOf(word) - 1) == '/')) {
                    set.addAll(maps.get(k));
                }
            }
        }
        if (set.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (String l:set) {
                for (String word:WebCrawler.BadWords) {
                    if (!l.contains(word)) {
                        continue;
                    }
                    if (l.startsWith(word)) {
                        l = l.replace(word, "<span style=\"color:red;\">" + word + "</span>");
                    }
                    else if (l.contains("//" + word)) {
                        l = l.replaceAll("//" + word, "//<span style=\"color:red;\">" + word + "</span>");
                    } else if (l.contains(" " + word)) {
                        l = l.replaceAll(" " + word, " <span style=\"color:red;\">" + word + "</span>");
                    }
                }
                sb.insert(0, l);
                sb.insert(0, "<br><br>");
            }
            return sb.toString();
        }
        return "";
    }

    @ResponseBody
    @GetMapping("cahier")
    public String cahier() throws Exception {
        int tail = 1;
        while (tail < 286) {
            tail++;
            Document document;
            log.info("tail: " + tail);
            document = Jsoup.connect("https://www.allocine.fr/presse-82005/critiques/cinema/?page=" + tail)
                    .get();


            //log.info("https://letterboxd.com/NickOfDaSouf/films/reviews/page/", page);
            //System.out.println(document);
            Elements sections = document.getElementsByClass("card entity-card entity-card-list cf hred");
            log.info("size: " + sections.size());
            if (sections.size() == 0) {
                break;
            }
            for (Element e : sections) {
                //System.out.println(e);
                com.example.demo.bean.Review record = new com.example.demo.bean.Review();
                Elements els = e.getElementsByClass("meta-title-link");
                Element e1 = els.get(0);
                record.setTitle(e1.text().trim());
                Elements els1 = e.getElementsByClass("stareval stareval-medium stareval-theme-default");
                Element e2 = els1.get(0);
                record.setNumber(Integer.valueOf(e2.text().trim().split(",")[0]));
                try {
                    log.info(e1.attr("href"));
                    document = Jsoup.connect("https://www.allocine.fr" + e1.attr("href"))
                            .get();
                } catch (Exception ex) {
                    Thread.sleep(1000);
                }
                Elements dates = document.getElementsByClass("meta-body-item meta-body-direction");
                if (dates.size() == 0) {
                    record.setDirector(null);
                } else {
                    Element d = dates.get(0);
                    record.setDirector(d.text().trim());
                }
                rankingMapper.saveReview(record);
                log.info("插入成功 {}", record);
            }
        }
        log.info("收集完毕");
        return "1";
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        HashSet<String> set = new HashSet<>();
        set.add("political drama//1970s//year 1977//year 1974//20th century//watergate//political leader//cold war era//u.s. politician//reference to richard nixon//american politician//interview//telephone call//reference to john f. kennedy//reference to leonid brezhnev//checkbook journalism//reference to henry kissinger//beverly hills hotel//reference to gerald ford//speaker phone//vietnam war//tv journalism//publicly disgraced//surnames as title//character names as title//the white house washington d.c.//washington post//flight//jumbo jet//flying first class//united states of america//worrying//reference to h.r. haldeman//fbi federal bureau of investigation//lincoln continental//nixon//tv talk show as subject//tv show as subject//talk show as subject//female nudity//president//resignation//scandal//battle of wits//guilt//reporter//telephone//chief of staff//camera//president of the united states//talk show host//apology//broken lightbulb//shoebox//reference to vidal sassoon//reference to edward kennedy//reference to lyndon b. johnson//reference to andrei gromyko//plaza hotel//reference to mike wallace//reference to william holden//american politics//sex//champagne//bentley the car//multiple narrators//father daughter relationship//photograph//cinerama theatre hollywood//obstruction of justice//taxes//flashback//flash forward//t 47//tv control booth//celebration//plaza hotel manhattan new york city//sardi's restaurant manhattan new york city//manhattan new york city//party//beach//running in place//advertisement//tv commercial//exoneration//banquet//wiretap//historical event//backstage//water//eating//tv ratings//phlebitis//wheelchair//gurney//reference to ibm//reference to alpo dog food//reference to general motors//confession//autograph//perspiration//brother brother relationship//father son relationship//death of brother//death//injustice//justice//cover up//motorcycle//photographer//media frenzy//witness//felony//reputation//los angeles international airport//taxi//hotel//nonlinear timeline//tv network executive//quaker//birthday cake//birthday//safe//tape recorder//los angeles times//reference to diahann carroll//helicopter//reference to jack anderson//reference to carl stern//reference to charles colson//reference to john ehrlichman//reference to john dean//motorcade//limousine//mirror//talking to the camera//recording//research//paranoia//tv news//impeachment//earphones//microphone//makeup artist//makeup//airport//hush money//u.s. constitution//lie//tyranny//u.s. supreme court//prayer//religion//reference to nikita khrushchev//reference to mao tse tung//reference to michael york//song//restaurant//eyeglasses//memory loss//london england//new york city//washington d.c.//literary agent//burglary//montage//watching tv//newsreel footage//golf//drink//drinking//attorney//lawyer//reference to the bee gees//voice over narration//san clemente california//gift//male male hug//hugging//nervousness//record album//record player//reel to reel tape recorder//exercise//newspaper clipping//bedroom//piano playing//handshake//glass of water//self confidence//tv monitor//mob of reporters//movie premiere//singing//singer//imitation//golf cart//car//sweating//rage//anger//speech//cigar smoking//pay phone//check//tv sponsor//los angeles california//alcoholic drink//self deprecation//escape artist//sydney australia//beverly hills california//airplane//handkerchief//memoir//husband wife relationship//hospital//television studio//skinny dipping//stripping//rear nudity//male nudity//nudity//cafeteria//australia//playboy bunny//comedian//political resignation//nixon resignation//author//television director//resigning from a job//shallowness//television camera//archive audio tape//italian shoes//shoes//cheeseburger//political aide//drunkenness//slash in title//f word//drunken telephone call//political cover up//dachshund//television host//tape recording//self loathing//self justification//sanctimony//researcher//republican//republican party//presidential aide//politics");
        for (String l:set) {
            String word = "male nudity";
            System.out.println(l.indexOf(word));
            System.out.println(l.charAt(l.indexOf(word) - 1));
                if (!l.contains(word)) {
                    continue;
                }
                if (l.indexOf(word) == 0 || (char)l.charAt(l.indexOf(word) - 1) == ' ' || l.charAt(l.indexOf(word) - 1) == '/') {
                    l = l.replaceAll(word, "<span style=\"color:red;\">" + word + "</span>");
                }

            sb.insert(0, l);
            sb.insert(0, "<br><br>");
        }
        System.out.println(sb.toString());
    }


}
