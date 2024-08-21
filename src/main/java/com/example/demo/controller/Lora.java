package com.example.demo.controller;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import jodd.util.Base64;
import jodd.util.StringUtil;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.*;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Lora implements Externalizable{

    CloseableHttpClient client = HttpClientBuilder.create().build();

    public static void main(String[] args) throws IOException {
        new Lora().listFiles("/Users/nick/Downloads/jw/10_jw", "jw");
        new ArrayBlockingQueue<>(1).add(1);
        new ReentrantLock().lock();
        new ThreadLocal<>().set(1);
        ClassLoader loader;
        Future<?> submit = ExecutorServiceUtil.newExecutorService().submit(new Runnable() {
            @Override
            public void run() {

            }
        });
        new Thread().start();
        new SynchronousQueue<>().add(1);
        Arrays.asList();
        new ConcurrentHashMap<>().put(1.1,2);
    }

    public void listFiles(String s, String nickname) throws IOException {
        File folder = new File(s);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                String pre = name.replace(".jpg", "");
                String all = nickname + ",";
                File newFile = new File(s + "/" + pre + ".txt");
                PrintWriter pw = new PrintWriter(newFile);
                pw.write(all);
                pw.flush();
                pw.close();
            }
        }
    }

    public static String file2Base64(File file) {
        if(file==null) {
            return null;
        }
        String base64 = null;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            byte[] buff = new byte[fin.available()];
            fin.read(buff);
            base64 = Base64.encodeToString(buff);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
