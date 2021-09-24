package com.example.demo.concurrent;

//import com.alibaba.dubbo.config.annotation.Service;
import com.example.demo.controller.Person;
import com.example.demo.controller.Player;
import lombok.Data;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Scope()
@Data
//@Service(version = "1.3", group = "g1")
public class B implements A{



    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        char[] chars = s.toCharArray();
        String valids = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Set<Character> records = new HashSet<>();
        for (int i = 0; i < valids.length(); i++) {
            records.add(valids.charAt(i));
        }
        while (left < right) {
            if (!records.contains(chars[left])) {
                left++;
                continue;
            }
            if (!records.contains(chars[right])) {
                right--;
                continue;
            }
            if (chars[left] == chars[right] || (chars[left] - '9' > 0 && chars[right] - '9' > 0 && Math.abs(chars[left] - chars[right]) == 32)) {
                left++;
                right--;
            }
            return false;
        }
        return true;
    }
    private static HttpGet get;
    public static void main(String[] args) {
        HttpClientConnectionManager poolingConnManager
                = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client
                = HttpClients.custom().setConnectionManager(poolingConnManager)
                .build();
        //client.execute(get);
    }
}
