package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.inject.Scope;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
//@Component
@Lazy
@Component
@Singleton
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name = "katy";
    private Integer age;
    public static void printDuplicateNames(String path1, String path2) throws IOException {
        // 读取第一个CSV的 Name -> URL 映射
        Map<String, String> nameUrlMap1 = new HashMap<>();
        List<String> lines1 = Files.readAllLines(Paths.get(path1));
        for (int i = 1; i < lines1.size(); i++) { // 从1开始跳过表头
            String[] parts = splitCsvLine(lines1.get(i));
            if (parts.length >= 3) {
                nameUrlMap1.put(parts[1].trim(), parts[3].trim()); // Name -> URL
            }
        }
        System.out.println(nameUrlMap1);

        // 读取第二个CSV中的所有名称
        Set<String> names2 = new HashSet<>();
        List<String> lines2 = Files.readAllLines(Paths.get(path2));
        boolean started = false;
        for (String line : lines2) {
            if (!started) {
                if (line.startsWith("Position,Name")) {
                    started = true;
                }
                continue;
            }
            String[] parts = splitCsvLine(line);
            if (parts.length >= 2) {
                names2.add(parts[1].trim());
            }
        }

        System.out.println(names2);
        // 查找重复的名称并打印 Name + URL
        for (Map.Entry<String, String> entry : nameUrlMap1.entrySet()) {
            if (names2.contains(entry.getKey())) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    // 简单的 CSV 分割（不处理复杂引号情况）
    private static String[] splitCsvLine(String line) {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1); // 支持含引号字段
    }

    public static void main(String[] args) throws IOException {
        printDuplicateNames("/Users/nick/Downloads/watchlist.csv", "/Users/nick/Downloads/imdb-1.csv");
    }
}
