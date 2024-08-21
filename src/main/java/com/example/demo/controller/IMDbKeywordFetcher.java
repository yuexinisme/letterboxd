package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import jodd.util.StringUtil;

public class IMDbKeywordFetcher {

    private static final String API_HOST = "imdb8.p.rapidapi.com";
    private static final String API_KEY = "your_rapidapi_key"; // Replace with your RapidAPI key

    public static List<String> getIMDbKeywords(String imdbId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://imdb-com.p.rapidapi.com/title/get-keywords?tconst=" + imdbId + "&limit=1000"))
                .header("x-rapidapi-key", "88d8eb3e99msh53940a4107d1005p16dd9djsn474c000e7da8")
                .header("x-rapidapi-host", "imdb-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        System.out.println("bodyL:" + body);
        MovieData movieData = null;
        List<String> keywords = new ArrayList<>();
        try {
            movieData = JSON.parseObject(body, MovieData.class);
            List<MovieData.Data.Title.ItemCategory> cats = movieData.getData().getTitle().getKeywordItemCategories();
            for (MovieData.Data.Title.ItemCategory c:cats) {
                MovieData.Data.Title.ItemCategory.Keywords ks = c.getKeywords();
                List<MovieData.Data.Title.ItemCategory.Keywords.Edge> edges = ks.getEdges();
                for (MovieData.Data.Title.ItemCategory.Keywords.Edge e:edges) {
                    MovieData.Data.Title.ItemCategory.Keywords.Edge.Node node = e.getNode();
                    String text = node.getKeyword().getText().getText();
                    if (!StringUtils.isNullOrEmpty(text)) {
                        keywords.add(text);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("转换失败 " + body);
            throw new Exception();
        }
        return keywords;
    }

    public static void main(String[] args) {
        try {
            List<String> keywords = getIMDbKeywords("tt0156701"); // Example IMDb ID for "The Shawshank Redemption"
            System.out.println("Keywords: " + keywords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

