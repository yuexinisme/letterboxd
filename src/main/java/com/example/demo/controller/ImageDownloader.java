package com.example.demo.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownloader {

    public static void main(String[] args) {
        String url = "https://gay9.com/blowbang/";
        try {
            downloadImages(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadImages(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements images = doc.select("img[data-src^=https://th.gay9.com/gaygalleries/]");

        for (Element img : images) {
            String imgUrl = img.attr("data-src");

            if (imgUrl.matches("https://th.gay9.com/gaygalleries/.+_\\d+\\.jpg")) {
                String baseUrl = imgUrl.replaceAll("_\\d+\\.jpg", "_");
                int index = 1;
                boolean moreImages = true;

                while (moreImages) {
                    String formattedIndex = String.format("%02dbig", index);
                    String newImageUrl = baseUrl + formattedIndex + ".jpg";
                    moreImages = downloadImage(newImageUrl, "pics/");
                    index++;
                }
            }
        }
    }

    private static boolean downloadImage(String imageUrl, String outputFolder) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                File folder = new File(outputFolder);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                try (FileOutputStream fos = new FileOutputStream(outputFolder + fileName)) {
                    fos.write(connection.getInputStream().readAllBytes());
                }
                System.out.println("Downloaded: " + imageUrl);
                return true;
            } else {
                System.out.println("No more images at: " + imageUrl);
                return false;
            }
        } catch (IOException e) {
            System.out.println("Failed to download image: " + imageUrl);
            return false;
        }
    }
}
