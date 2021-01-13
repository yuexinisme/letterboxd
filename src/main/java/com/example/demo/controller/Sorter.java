package com.example.demo.controller;

public class Sorter {

    public static void quickSort(int[] nums, int left, int right) {
        if (left >= right) return;
        int mid = quickSortRange(nums, left, right);
        quickSort(nums, left, mid - 1);
        quickSort(nums, mid + 1, right);
    }

    //7，6，4，2，1
    //7 6 1 2 x

    private static int quickSortRange(int[] nums, int left, int right) {
        int key = nums[left];
        while (left <= right) {
            while (nums[right] >= key) {
                right--;
            }
            if (left < right) {
                exchange(nums, left, right);
            }
            while (nums[left] <= key) {
                left++;
            }
            if (left < right) {
                exchange(nums, left, right);
            }
        }
        return 0;
    }

    private static void exchange(int[] nums, int left, int right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }
}
