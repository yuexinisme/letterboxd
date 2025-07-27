package com.example.demo.controller;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.regex.*;

public class SRTTimeShifter {

    public static void shiftSRTTimes(String filePath, int shiftSeconds) throws IOException {
        Charset encoding = Charset.forName("ISO-8859-1"); // 或 Windows-1252
        Path path = Paths.get(filePath);

        // 手动读取文件内容
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(path, encoding)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        // 匹配时间戳
        Pattern pattern = Pattern.compile(
                "(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})\\s-->\\s(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})"
        );

        StringBuilder result = new StringBuilder();
        Matcher matcher = pattern.matcher(content.toString());

        int lastEnd = 0;
        while (matcher.find()) {
            result.append(content, lastEnd, matcher.start());

            String originalLine = matcher.group(0);
            System.out.println("处理行: " + originalLine);

            try {
                String start = shiftTime(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2)),
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        -shiftSeconds
                );
                String end = shiftTime(
                        Integer.parseInt(matcher.group(5)),
                        Integer.parseInt(matcher.group(6)),
                        Integer.parseInt(matcher.group(7)),
                        Integer.parseInt(matcher.group(8)),
                        -shiftSeconds
                );
                result.append(start).append(" --> ").append(end);
            } catch (Exception e) {
                System.err.println("❌ 出错，保留原样: " + originalLine);
                result.append(originalLine);
            }

            lastEnd = matcher.end();
        }

        result.append(content.substring(lastEnd));

        // 写回文件（使用同样编码）
        try (BufferedWriter writer = Files.newBufferedWriter(path, encoding)) {
            writer.write(result.toString());
        }
    }

    private static String shiftTime(int h, int m, int s, int ms, int shiftSeconds) {
        int totalMs = ((h * 3600 + m * 60 + s) + shiftSeconds) * 1000 + ms;
        if (totalMs < 0) totalMs = 0;

        int newH = totalMs / 3600000;
        int newM = (totalMs % 3600000) / 60000;
        int newS = (totalMs % 60000) / 1000;
        int newMs = totalMs % 1000;

        return String.format("%02d:%02d:%02d,%03d", newH, newM, newS, newMs);
    }

    public static void main(String[] args) {
        String path = "/Users/nick/Downloads/to.be.and.to.have.(2002).fre.1cd.(4579585)/etreetavoir-1cd.srt.srt";

        try {
            shiftSRTTimes(path, 10);
            System.out.println("✅ 所有时间戳处理完成！");
        } catch (IOException e) {
            System.err.println("⚠️ 文件读取或写入失败: " + e.getMessage());
        }
    }
}
