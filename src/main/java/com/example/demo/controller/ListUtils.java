package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    /**
     * Retains only the elements that are present in both lists.
     *
     * @param list1 The first list.
     * @param list2 The second list.
     * @return A new list containing only the elements that are present in both lists.
     */
    public static <T> List<T> retainCommonElements(List<T> list1, List<T> list2) {
        // Create a copy of the first list to avoid modifying the original list
        List<T> commonElements = new ArrayList<>(list1);
        // Retain only the elements that are present in both lists
        commonElements.retainAll(list2);
        return commonElements;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("apple");
        list1.add("banana");
        list1.add("cherry");

        List<String> list2 = new ArrayList<>();
        list2.add("banana");
        list2.add("cherry");
        list2.add("date");

        List<String> commonElements = retainCommonElements(list1, list2);
        System.out.println("Common elements: " + commonElements);
    }
}

