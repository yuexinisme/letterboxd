package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;

@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    private void readObject(ObjectInputStream in) throws Exception {
        name = in.readUTF();
    }
}