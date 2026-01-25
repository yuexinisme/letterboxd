package com.example.demo.controller;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Slf4j
@Component
public class PlaywrightLetterboxdScraper {

    //@Scheduled(fixedRate = 1000 * 60 * 60 * 12) // 每12小时执行一次
    public Map<String, Integer> collectLikesWithPlaywright() {
        log.info("开始使用Playwright收集数据...");
        Date startTime = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String, Integer> result = new HashMap<>();
        int totalInserted = 0;

        // 创建Playwright实例
        try (Playwright playwright = Playwright.create()) {
            // 配置浏览器启动选项
            BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(true)  // 无头模式，设为false可看到浏览器
                    .setSlowMo(50)      // 减慢操作，更自然
                    .setArgs(Arrays.asList(
                            "--disable-blink-features=AutomationControlled",
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    ));

            // 启动浏览器（自动下载）
            try (Browser browser = playwright.chromium().launch(launchOptions)) {
                // 创建浏览器上下文（相当于隐身窗口）
                Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .setViewportSize(1920, 1080)
                        .setLocale("en-US");

                try (BrowserContext context = browser.newContext(contextOptions)) {
                    // 创建页面
                    Page page = context.newPage();

                    // 拦截不必要的资源请求，加快速度
                    setupRequestInterception(page);

                    // 数据库连接
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/test",
                            "root",
                            "yuexinisme"
                    );

                    try {
                        // 1. 获取所有电影链接
                        Set<String> movieUrls = getAllMovieUrls(page);

                        // 2. 处理每个电影的点赞
                        for (String movieUrl : movieUrls) {
                            int inserted = processMovieLikes(page, conn, movieUrl);
                            totalInserted += inserted;

                            // 延迟避免被封
                            page.waitForTimeout(2000 + new Random().nextInt(3000));
                        }

                        Date endTime = new Date();
                        log.info("收集完成！");
                        log.info("开始时间: {}", f.format(startTime));
                        log.info("结束时间: {}", f.format(endTime));
                        log.info("用时: {} 分钟",
                                (endTime.getTime() - startTime.getTime()) / 1000 / 60);
                        log.info("总共插入: {} 条记录", totalInserted);

                    } finally {
                        conn.close();
                    }
                }
            }

        } catch (Exception e) {
            log.error("Playwright爬取失败", e);
        }

        return result;
    }

    private void setupRequestInterception(Page page) {
        // 拦截不必要的请求，加快页面加载
        page.route("**/*.{png,jpg,jpeg,gif,css,woff,woff2}", route -> route.abort());

        // 或者只允许必要的请求
        page.route("**/*", route -> {
            String url = route.request().url();
            if (url.endsWith(".css") || url.endsWith(".png") || url.endsWith(".jpg")) {
                route.abort();
            } else {
                route.resume();
            }
        });
    }

    private Set<String> getAllMovieUrls(Page page) {
        Set<String> movieUrls = new HashSet<>();

        try {
            for (int pageNum = 1; pageNum <= 10; pageNum++) {
                String reviewUrl = String.format(
                        "https://letterboxd.com/NickOfDaSouth/films/reviews/page/%d/",
                        pageNum
                );

                log.info("获取电影列表页面: {}", reviewUrl);

                // 导航到页面
                Response response = page.navigate(reviewUrl,
                        new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));

                // 检查响应状态
                if (!response.ok()) {
                    log.error("页面访问失败: {} - {}", reviewUrl, response.status());
                    break;
                }

                // 等待关键元素出现
                try {
                    page.waitForSelector(".like-link-target",
                            new Page.WaitForSelectorOptions().setTimeout(10000));
                } catch (Exception e) {
                    log.info("第 {} 页没有找到电影链接，停止", pageNum);
                    break;
                }

                // 使用JavaScript提取所有电影链接
                List<String> moviePaths = (List<String>) page.evalOnSelectorAll(
                        ".like-link-target[data-likes-page]",
                        "elements => elements.map(el => el.getAttribute('data-likes-page'))"
                );

                if (moviePaths.isEmpty()) {
                    log.info("第 {} 页没有电影数据，停止", pageNum);
                    break;
                }

                // 构建完整URL
                for (String path : moviePaths) {
                    String fullUrl = "https://letterboxd.com" + path;
                    movieUrls.add(fullUrl);
                    log.info("找到电影: {}", fullUrl);
                }

                log.info("第 {} 页找到 {} 个电影", pageNum, moviePaths.size());

                // 页面间延迟
                page.waitForTimeout(3000);
            }

        } catch (Exception e) {
            log.error("获取电影列表失败", e);
            throw e;
        }

        log.info("总共找到 {} 个电影", movieUrls.size());
        return movieUrls;
    }

    private int processMovieLikes(Page page, Connection conn, String movieUrl) throws Exception {
        log.info("处理电影点赞: {}", movieUrl);
        int insertedCount = 0;

        for (int pageNum = 1; pageNum <= 100; pageNum++) {
            String likesUrl = movieUrl + "page/" + pageNum + "/";
            log.info("访问点赞页面: {}", likesUrl);

            try {
                // 导航到点赞页面
                Response response = page.navigate(likesUrl,
                        new Page.NavigateOptions()
                                .setWaitUntil(WaitUntilState.NETWORKIDLE)
                                .setReferer(movieUrl));

                if (!response.ok()) {
                    log.error("页面访问失败: {} - {}", likesUrl, response.status());
                    break;
                }

                // 等待用户数据出现
                try {
                    page.waitForSelector("h3.title-3",
                            new Page.WaitForSelectorOptions().setTimeout(5000));
                } catch (Exception e) {
                    log.info("第 {} 页没有找到用户数据，停止", pageNum);
                    break;
                }

                // 提取所有用户名
                List<String> userNames = (List<String>) page.evalOnSelectorAll(
                        "h3.title-3",
                        "elements => elements.map(el => el.textContent.trim())"
                );

                if (userNames.isEmpty()) {
                    log.info("第 {} 页没有用户数据，停止", pageNum);
                    break;
                }

                log.info("第 {} 页找到 {} 个用户", pageNum, userNames.size());

                // 处理用户数据
                boolean hasNew = false;
                for (String userName : userNames) {
                    if (saveUserToDatabase(conn, userName, movieUrl)) {
                        insertedCount++;
                        hasNew = true;
                        log.info("插入用户: {} - {}", userName, movieUrl);
                    }
                }

                // 如果本页没有新数据，停止翻页
                if (!hasNew) {
                    log.info("本页没有新数据，停止翻页");
                    break;
                }

                // 页面间延迟
                page.waitForTimeout(1500);

            } catch (Exception e) {
                log.error("处理页面失败: {}, 错误: {}", likesUrl, e.getMessage());
                break;
            }
        }

        log.info("电影 {} 处理完成，插入 {} 条记录", movieUrl, insertedCount);
        return insertedCount;
    }

    private boolean saveUserToDatabase(Connection conn, String userName, String movieUrl) throws SQLException {
        // 检查是否已存在
        String checkSql = "SELECT COUNT(*) FROM likes WHERE name = ? AND movie = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, userName);
            checkStmt.setString(2, movieUrl);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int exists = rs.getInt(1);

            if (exists > 0) {
                log.debug("用户已存在: {} - {}", userName, movieUrl);
                return false;
            }
        }

        // 插入新记录
        String insertSql = "INSERT INTO likes (name, movie) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, userName);
            insertStmt.setString(2, movieUrl);
            insertStmt.executeUpdate();
            return true;
        }
    }

    // 简化版本 - 快速测试用
    public void quickTest() {
        log.info("开始快速测试...");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(false) // 设为true看不到浏览器
            );

            Page page = browser.newPage();

            // 访问测试页面
            page.navigate("https://letterboxd.com/NickOfDaSouth/films/reviews/page/1/");

            // 等待并截图
            page.waitForTimeout(5000);
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("letterboxd-test.png")));

            // 获取页面标题
            String title = page.title();
            log.info("页面标题: {}", title);

            // 获取电影数量
            int movieCount = page.locator(".like-link-target").count();
            log.info("找到 {} 个电影", movieCount);

            browser.close();

        } catch (Exception e) {
            log.error("测试失败", e);
        }
    }
}