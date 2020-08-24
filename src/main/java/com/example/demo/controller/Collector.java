package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class Collector {

    /**
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Integer> collectLikes() throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "yuexinisme");
        PreparedStatement statement = conn.prepareStatement("insert into likes (name, movie) values (?,?)");
        PreparedStatement checkState = conn.prepareStatement("select * from likes where id=? and movie=?");
        Map<String, Integer> res = new HashMap<String, Integer>();
        for (int page = 1; page <= 22; page++) {
            Document document = Jsoup.connect("https://letterboxd.com/yuexinisme/films/reviews/page/" + page)
                    .get();
            System.out.println("https://letterboxd.com/yuexinisme/films/reviews/page/" + page);
            //System.out.println(document);
            Elements names = document.getElementsByClass("like-link-target react-component -monotone");
            for (Element e:names) {
                //System.out.println(e);
                String url = e.attr("data-likes-page");
                String fullUrl = "https://letterboxd.com" + url;
                System.out.println(fullUrl);
                for (int p = 1;;p++) {
                    Document doc = Jsoup.connect(fullUrl + "page/" + p)
                            .get();
                    Elements els = doc.select("h3[class=title-3]");
                    if (els.size() == 0)
                        break;
                    for (Element e1:els) {
                        String name = e1.text();
                        System.out.println(name);
                        statement.setString(1, name);
                        statement.setString(2, fullUrl);
                        checkState.setString(1, name);
                        checkState.setString(2, fullUrl);
                        if (!checkState.executeQuery().next())
                            statement.execute();
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
        collectLikes();
    }


}
