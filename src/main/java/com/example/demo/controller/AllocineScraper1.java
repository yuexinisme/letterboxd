package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class AllocineScraper1 {
    static final String BASE_URL = "https://www.allocine.fr/presse-11005/critiques/cinema/?page=";
    static final String DETAIL_PREFIX = "https://www.allocine.fr";
    static Connection connection;
    static Set<String> movieSet = new HashSet<>();

    public static void main(String[] args) throws Exception {
        initDB();
        loadPendingMovies();

        int page = 2;
        while (page < 3) {
            System.out.println("📄 正在处理第 " + page + " 页...");
            Document doc = Jsoup.connect(BASE_URL + page).get();
            Elements cards = doc.select(".card.entity-card.entity-card-list.cf.hred");

            if (cards.isEmpty()) {
                System.out.println("✅ 没有更多内容，结束。");
                break;
            }

            for (Element card : cards) {
                try {
                    Element ratingSpan = card.selectFirst(".rating-item-content .stareval-note");
                    if (ratingSpan == null) continue;

                    // 提取 name 和 base64 链接
                    Element titleSpan = card.selectFirst("span[class$=meta-title-link]");
                    if (titleSpan == null) continue;
                    String name = titleSpan.text().trim();

                    String encoded = titleSpan.className().replace("meta-title-link", "").trim();
                    encoded = encoded.replace("ACrL2ZACrpbG0vZmljaGVmaWxtX2dlbl9jZmlsbT0", "L2ZpbG0vZmljaGVmaWxtX2dlbl9jZmlsbT0");
                    String decoded = new String(Base64.getDecoder().decode(encoded));
                    String filmUrl = DETAIL_PREFIX + decoded;

                    // 如果 name 不在需要更新的记录中，跳过
                    boolean matchFound = movieSet.stream().anyMatch(k -> k.startsWith(name + "|"));
                    if (!matchFound) {
                        System.out.println("⏩ 跳过未处理电影（未匹配 name）: " + name);
                        continue;
                    }

                    Document filmDoc = Jsoup.connect(filmUrl).timeout(10000).get();

                    String director = "";
                    Element directionDiv = filmDoc.selectFirst(".meta-body-item.meta-body-direction");
                    if (directionDiv != null) {
                        Element directorSpan = directionDiv.selectFirst("span.dark-grey-link");
                        if (directorSpan != null) {
                            director = directorSpan.text().trim();
                        }
                    }

                    String key = name + "|" + director;
                    if (!movieSet.contains(key)) {
                        System.out.println("⏩ 跳过未处理电影（匹配 name 但不匹配导演）: " + name + " - " + director);
                        continue;
                    }

                    // 提取评分
                    Element starevalDiv = filmDoc.selectFirst("div.stareval.stareval-small.stareval-theme-default");
                    if (starevalDiv == null) continue;

                    String avgRatingStr = starevalDiv.selectFirst("span.stareval-note").text().trim().replace(",", ".");
                    int averageRating = (int) (Double.parseDouble(avgRatingStr) * 10);

                    String reviewText = starevalDiv.selectFirst("span.stareval-review").text().trim();
                    int mediaCount = Integer.parseInt(reviewText.replaceAll("\\D+", ""));

                    System.out.println("🎬 电影: " + name);
                    System.out.println("🎬 导演: " + director);
                    System.out.println("⭐ 平均评分: " + averageRating);
                    System.out.println("🗞️ 媒体数量: " + mediaCount);

                    updateAverageAndCount(name, director, averageRating, mediaCount);

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
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC",
                "root",
                "yuexinisme"
        );
    }

    static void loadPendingMovies() throws SQLException {
        String sql = "SELECT name, director FROM movies WHERE average_rating IS NULL OR media_count IS NULL";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String name = rs.getString("name").trim();
            String director = rs.getString("director").trim();
            movieSet.add(name + "|" + director);
        }
        System.out.println("📥 需要处理的电影记录数量: " + movieSet.size());
    }

    static void updateAverageAndCount(String name, String director, int averageRating, int mediaCount) throws SQLException {
        String updateSql = "UPDATE movies SET average_rating = ?, media_count = ? WHERE name = ? AND director = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateSql);
        updateStmt.setInt(1, averageRating);
        updateStmt.setInt(2, mediaCount);
        updateStmt.setString(3, name);
        updateStmt.setString(4, director);

        int rowsAffected = updateStmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("🔁 已更新评分和媒体数量: " + averageRating + " | " + mediaCount + "\n");
        } else {
            System.out.println("❌ 未找到对应记录，跳过更新: " + name + " - " + director + "\n");
        }
    }
}
