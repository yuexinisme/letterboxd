package com.example.demo.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;

public class GettyImageDownloader {

    private static final String BASE_URL = "https://www.gettyimages.com";
    private static String SEARCH_URL_TEMPLATE = BASE_URL + "/search/2/image?family=editorial&phrase=%s&sort=newest&page=%d";

    public static void main(String[] args) throws Exception {
        String searchTerm = "\"Sven Groeneveld\""; // Replace with your search term
        //SEARCH_URL_TEMPLATE += "&numberofpeople=one";
        searchTerm = URLEncoder.encode(searchTerm, "utf-8");
        downloadImages(searchTerm);
    }

    public static void downloadImages(String searchTerm) {
        int pageNum = 1;
        boolean hasNextPage = true;

        // Create the directory if it doesn't exist
        File directory = new File("Getty");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        while (hasNextPage) {
            if (pageNum == 20) {
                break;
            }
            try {
                String url = String.format(SEARCH_URL_TEMPLATE, searchTerm, pageNum);
                Document doc = Jsoup.connect(url).get();

                // Get all URLs that start with "/detail"
                Elements detailLinks = doc.select("a[href^=/detail]");
                hasNextPage = !detailLinks.isEmpty();

                for (Element link : detailLinks) {
                    String imageUrl = BASE_URL + link.attr("href");
                    System.out.println("detail: " + imageUrl);
                    // Request the detail page
                    Document imageDoc = Jsoup.connect(imageUrl).get();
                    Elements sourceTags = imageDoc.select("source[type=image/jpeg]");
                    System.out.println("size:" + sourceTags.size());
                    for (Element sourceTag : sourceTags) {
                        String srcset = sourceTag.attr("srcset");
                        // Check if it contains "2048x2048"
                        if (srcset.contains("2048x2048")) {
                            String imageUrlToDownload = srcset.split(",")[0].trim(); // Get the first URL
                            downloadImage(imageUrlToDownload);
                        }
                    }
                }
                pageNum++;
            } catch (IOException e) {
                System.err.println("Error fetching page: " + e.getMessage());
                 // Stop the loop on error
            }
        }
    }

    private static void downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();

            // Extracting the filename from the URL without query parameters
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.indexOf("?"));

            // If there's no query parameter, use the whole name
            if (fileName.isEmpty() || !imageUrl.contains("?")) {
                fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            }

            File outputFile = new File("Getty", System.currentTimeMillis() + fileName);

            // Create directory if it doesn't exist
            File directory = new File("Getty");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Downloaded: " + outputFile.getAbsolutePath());
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to download image: " + imageUrl + " - " + e.getMessage());
        }
    }

}



