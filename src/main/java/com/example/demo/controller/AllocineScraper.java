package com.example.demo.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.Base64;

public class AllocineScraper {
    static final String BASE_URL = "https://www.allocine.fr/presse-11005/critiques/cinema/?page=";
    static final String DETAIL_PREFIX = "https://www.allocine.fr";
    static final String DETAIL_SUFFIX = ".html";

    static Connection connection;

    public static void main(String[] args) throws Exception {
        initDB();

        int page = 2;
        while (true) {
            if (page >= 3) {
                break;
            }
            System.out.println("📄 正在处理第 " + page + " 页...");
            Document doc = Jsoup.connect(BASE_URL + page).get();

            Elements cards = doc.select(".card.entity-card.entity-card-list.cf.hred");
            if (cards.isEmpty()) {
                System.out.println("✅ 没有更多内容，结束。");
                break;
            }

            for (Element card : cards) {
                try {
                    // 评分
                    Element ratingSpan = card.selectFirst(".rating-item-content .stareval-note");
                    if (ratingSpan == null) continue;
                    String ratingText = ratingSpan.text();
                    String rating = ratingText.split(",")[0].trim();

                    // 提取 meta-title-link a 元素
                    Element aTag = card.selectFirst("span[class$=meta-title-link]");
                    if (aTag == null) continue;
                    String aClass = aTag.className().trim();

                    // 获取并处理 base64
                    String encoded = null;
                    encoded = aClass.replace("meta-title-link", "").trim();
                    encoded = encoded.replace("ACrL2ZACrpbG0vZmljaGVmaWxtX2dlbl9jZmlsbT0", "L2ZpbG0vZmljaGVmaWxtX2dlbl9jZmlsbT0");

                    byte[] decodedBytes = Base64.getDecoder().decode(encoded);
                    String decoded = new String(decodedBytes);
                    String filmUrl = DETAIL_PREFIX + decoded;

                    Document filmDoc = Jsoup.connect(filmUrl).get();
                    String name = filmDoc.selectFirst(".titlebar-title.titlebar-title-xl").text().trim();

                    Element directionDiv = filmDoc.selectFirst(".meta-body-item.meta-body-direction");
                    String director = "";

                    if (directionDiv != null) {
                        Element directorSpan = directionDiv.selectFirst("span.dark-grey-link");
                        if (directorSpan != null) {
                            director = directorSpan.text().trim();
                        }
                    }
                    System.out.println(director);

                    System.out.println("🎬 电影: " + name);
                    System.out.println("🎬 导演: " + director);
                    System.out.println("⭐ 评分: " + rating);

                    insertIfNotExists(name, director, rating);
                } catch (Exception ex) {
                    System.out.println("⚠️ 出现异常，跳过该项：" + ex.getMessage());
                }
            }

            page++;
        }

        connection.close();
    }

    static void initDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "root", "yuexinisme");

        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255)," +
                "director VARCHAR(255)," +
                "rating VARCHAR(10)," +
                "UNIQUE KEY unique_director_name (director, name))");
    }

    static void insertIfNotExists(String name, String director, String rating) throws SQLException {
        String query = "SELECT COUNT(*) FROM movies WHERE name=? AND director=?";
        PreparedStatement checkStmt = connection.prepareStatement(query);
        checkStmt.setString(1, name);
        checkStmt.setString(2, director);
        ResultSet rs = checkStmt.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0) {
            System.out.println("⏭️ 已存在，跳过插入");
            return;
        }

        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO movies(name, director, rating) VALUES (?, ?, ?)");
        insertStmt.setString(1, name);
        insertStmt.setString(2, director);
        insertStmt.setString(3, rating);
        insertStmt.executeUpdate();
        System.out.println("✅ 已插入数据库\n");
    }
}