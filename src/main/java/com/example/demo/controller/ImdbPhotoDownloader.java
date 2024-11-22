package com.example.demo.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImdbPhotoDownloader {

    // Set up Selenium WebDriver
    public static WebDriver setupWebDriver() {
        // Set the path to your ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/Users/nick/Downloads/chromedriver-mac-x64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Run Chrome in headless mode (without GUI)
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    public static void downloadPhotos(String imdbId) throws IOException {
        WebDriver driver = setupWebDriver();
        String url = "https://www.imdb.com/name/" + imdbId + "/mediaindex";
        driver.get(url);

        // Scroll down to load more images until no new images appear
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        int scrollHeight = 0;
        while (true) {
            // Scroll down by a large amount
            jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            // Wait for new images to load (adjust the sleep time if needed)
            try {
                Thread.sleep(2000); // 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check if we have reached the bottom (no new images loaded)
            int newScrollHeight = Integer.parseInt(jsExecutor.executeScript("return document.body.scrollHeight;").toString());
            if (newScrollHeight == scrollHeight) {
                break; // No more scrolling possible
            }
            scrollHeight = newScrollHeight;
        }


        // Now collect all image URLs after the page has fully loaded
        List<WebElement> images = driver.findElements(By.cssSelector(".media_index_thumb_list img"));
        System.out.println("image size:" + images.size());
        // Download each image
        for (WebElement img : images) {
            String thumbnailUrl = img.getAttribute("loadlate"); // Get the thumbnail URL
            if (!thumbnailUrl.isEmpty()) {
                String fullImageUrl = getFullImageUrl(thumbnailUrl); // Modify thumbnail URL to get full-size image URL
                downloadImage(fullImageUrl, imdbId); // Download the image
            }
        }

        // Close the driver
        driver.quit();
    }

    // Function to download an image from a URL
    private static void downloadImage(String imageUrl, String imdbId) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Create a folder for the IMDb ID if it doesn't exist
        File dir = new File(imdbId);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // File name derived from image URL
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        File outputFile = new File(dir, fileName);

        // Download the image
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            System.out.println("Downloaded: " + fileName);
        }
    }

    // Function to get the full-size image URL from the thumbnail URL
    private static String getFullImageUrl(String thumbnailUrl) {
        // Full-size images usually have the _V1_ part removed or modified
        // Example: https://m.media-amazon.com/images/M/MV5BMTk4ODUzMzM3NF5BMl5BanBnXkFtZTcwOTY0NDg0Mw@@._V1_UX100_CR0,0,100,100_AL_.jpg
        // Remove the _V1_ section to get the full-size image:
        return thumbnailUrl.replaceAll("\\._V1_.*\\.jpg", ".jpg");
    }

    public static void main(String[] args) {
        try {
            // Example IMDb person ID
            downloadPhotos("nm0000146");  // Tom Hanks' IMDb ID
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
