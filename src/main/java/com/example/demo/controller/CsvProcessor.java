package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Bean {
    private int number;
    private String name;
    private int year;
    private String url;
    private String tags;

    // Getters and setters
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    @Override
    public String toString() {
        return number + "," + name + "," + year + "," + url + "," + tags;
    }
}

public class CsvProcessor {

    public static void processCsv(String csvFilePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(csvFilePath));
        List<Bean> beans = new ArrayList<>();

        // Convert lines to Bean objects
        for (String line : lines) {
            try {
                System.out.println(line);
                String[] fields = line.split(",");
                Bean bean = new Bean();
                bean.setNumber(Integer.parseInt(fields[0]));
                bean.setName(fields[1]);
                bean.setYear(Integer.parseInt(fields[2]));
                bean.setUrl(fields[3]);
                bean.setTags(fields.length > 4 ? fields[4] : "");
                beans.add(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Iterate over beans and update tags if empty
        int count = 0;
        for (int i = 24100; i < beans.size(); i++) {
            if (count == 550) {
                break;
            }
            Bean bean = beans.get(i);
            if (bean.getTags().isEmpty()) {
                String newTags = methodB(bean.getUrl());
                bean.setTags(newTags);

                // Update the corresponding line in the CSV
                lines.set(i, bean.toString());

                // Write the updated line back to the file immediately
                Files.write(Paths.get(csvFilePath), lines);
                count++;
            }
        }
    }

    // Method B to return a string based on the URL
    private static String methodB(String url) {
        System.out.println(url);
        try {
            Document newDoc = null;
            try {
                newDoc = Jsoup.connect(url).get();
            } catch (IOException e) {
                try {
                    Thread.sleep(3000);
                    newDoc = Jsoup.connect(url).get();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return "";
                }
            }

            // Find the element with data-track-action="IMDb"
            Element imdbElement = newDoc.selectFirst("[data-track-action=IMDb]");
            if (imdbElement != null) {
                String imdbHref = imdbElement.attr("href");
                if (!imdbHref.isEmpty()) {
                    String[] split = imdbHref.split("/");
                    String imdbId = split[split.length - 2];
                    List<String> keywords = null;
                    try {
                        keywords = IMDbKeywordFetcher.getIMDbKeywords(imdbId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "";
                    }
                    System.out.println("keywords:" + keywords);
                    return String.join("//", keywords);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static void main(String[] args) {
        try {
            processCsv("/Users/nick/Desktop/xx.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

