package com.example.demo.test;

import jodd.io.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Customer {

    Customer() {
        System.out.println("gy");
    }

    public static void main(String[] args) throws Exception {
        String f1 = "/Users/nick/Desktop/diary.csv",
                f2 = "/Users/nick/Desktop/diary2.csv";
        String[] lines = FileUtil.readLines(f1);
        for (String line:lines) {
            if (line.contains("male nudity") || line.startsWith("Date")) {
                FileUtil.appendString(f2, line + "\n");
            }
        }
    }
}
