package com.example.demo.controller;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class RandomFileNameRenamer {

    public static void main(String[] args) {
        // 文件夹路径
        String folderPath = "/Users/nick/Downloads/ftm";

        // 获取文件夹
        File folder = new File(folderPath);

        // 检查文件夹是否存在并且是一个目录
        if (folder.exists() && folder.isDirectory()) {
            // 获取文件夹下所有文件
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // 获取文件的扩展名（后缀）
                        String fileName = file.getName();
                        String fileExtension = "";
                        int dotIndex = fileName.lastIndexOf(".");
                        if (dotIndex > 0) {
                            fileExtension = fileName.substring(dotIndex); // 获取后缀
                        }

                        // 生成随机文件名
                        String randomFileName = UUID.randomUUID().toString() + fileExtension;

                        // 创建新的文件对象
                        File renamedFile = new File(file.getParent(), randomFileName);

                        // 重命名文件
                        boolean success = file.renameTo(renamedFile);
                        if (success) {
                            System.out.println("File renamed: " + file.getName() + " -> " + randomFileName);
                        } else {
                            System.out.println("Failed to rename file: " + file.getName());
                        }
                    }
                }
            }
        } else {
            new HashMap<>().put(1,1);
            new String("");
            System.out.println("Provided path is not a valid directory.");
        }
    }
}
