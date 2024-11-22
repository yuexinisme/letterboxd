package com.example.demo.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebCrawler {

    static public final List<String> BadWords = new ArrayList<>();

    static final private ExecutorService pool = Executors.newFixedThreadPool(2);

    static {
        BadWords.add("male nudity");
        BadWords.add("male rape");
        BadWords.add("gay rape");
        BadWords.add("gay sex");
        BadWords.add("male frontal nudity");
        BadWords.add("male rear nudity");
        BadWords.add("gay cinema");
        BadWords.add("gay protagonist");
        BadWords.add("lgbt");
        BadWords.add("older man younger man relationship");
        BadWords.add("explicit sex");
        BadWords.add("gay interest");
        BadWords.add("male full frontal nudity");
        BadWords.add("erection");
        BadWords.add("homoerotic");
        BadWords.add("penis");
        BadWords.add("unsimulated sex");
        BadWords.add("homoerotism");
        BadWords.add("homosexual");
        BadWords.add("queer");
        BadWords.add("male pubic hair");
        BadWords.add("group sex");
        BadWords.add("illuminati");
        BadWords.add("cunnilingus");
        BadWords.add("anal rape");
        BadWords.add("anal sex");
        BadWords.add("kissing someone's breasts");
        BadWords.add("69 sex position");
        BadWords.add("ancient astronaut");
        BadWords.add("bareback sex");
        BadWords.add("breast sucking");
        BadWords.add("older man younger man");
        BadWords.add("older man younger woman");
        BadWords.add("male prostitution");
        BadWords.add("male prostitute");
        BadWords.add("forced to watch rape");
        BadWords.add("cavity");
        BadWords.add("sex ritual");
        BadWords.add("naked");
        BadWords.add("breast fondling");
    }

    public static List<String> crawlUrls(String url) throws Exception {
        List<String> imdbLinks = new ArrayList<>();

        // Fetch the initial page
        Document doc = Jsoup.connect(url).get();

        // Find all elements where class starts with "really-lazy-load poster"
        Elements elements = doc.select("[class^=really-lazy-load]");
        List<Future> futures = new ArrayList<>();
        for (Element element : elements) {

            String relativeLink = element.attr("data-target-link");
            if (!relativeLink.isEmpty()) {
                String fullUrl = "https://letterboxd.com" + relativeLink;
                System.out.println(fullUrl);
                // Fetch the new page
                Document newDoc = null;
                try {
                    newDoc = Jsoup.connect(fullUrl).get();
                } catch (IOException e) {
                    try {
                        Thread.sleep(3000);
                        newDoc = Jsoup.connect(fullUrl).get();
                    } catch (Exception e1) {
                        continue;
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
                            continue;
                        }
                        System.out.println("keywords:" + keywords);
                        List<String> words = ListUtils.retainCommonElements(keywords, BadWords);
                        if (!words.isEmpty()) {
                            System.out.println("match:" + words);
                            imdbLinks.add(imdbHref + ":" + words);
                        }
                    }
                }
            }
            // Get the data-target-link attribute and prepend the base URL

        }
        return imdbLinks;
    }

    public static void main(String[] args) {
        try {
            String url = "https://letterboxd.com/000_leo/list/tspdt-starting-list-2/detail/by/rating/"; // Replace with your URL
            List<String> links = crawlUrls(url);
            for (String link : links) {
                System.out.println(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


