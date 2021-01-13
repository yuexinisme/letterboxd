package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        if (candidates.length == 0) {
            return res;
        }
        backtrack(res, candidates, target, 0, new ArrayList<Integer>());
        return res;
    }

    private void backtrack(List<List<Integer>> res, int[] candidates, int target, int start, List<Integer> group) {

        int total = 0;
        for (Integer n:group) {
            total += n;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > 0 && candidates[i] == candidates[i - 1] && group.get(group.size() - 1) != candidates[i]) {
                continue;
            }
            if (total + candidates[i] < target) {
                group.add(candidates[i]);
                backtrack(res, candidates, target, i, new ArrayList<Integer>(group));
                group.remove(group.size() - 1);
            } else if (total + candidates[i] == target) {
                res.add(new ArrayList<Integer>(group));
                break;
            } else {
                break;
            }
        }

    }

    public int reverse(int x) {
        int res = 0;
        if (x == 0) {
            return 0;
        }
        while (x != 0) {
            int left = x % 10;
            if (x > 0) {
                if (Integer.MAX_VALUE/10 < res || (Integer.MAX_VALUE/10 == res && Integer.MAX_VALUE % 10 < left)) {
                    return 0;}
            } else {
                if (Integer.MIN_VALUE/10 > res || (Integer.MIN_VALUE/10 == res && Integer.MAX_VALUE % 10 > left)) {
                    return 0;}
            }
            res = 10 * res + left;
            x /= 10;
        }
        return res;
    }

    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        int reversed = 0;
        while (x != 0) {
            int left = x % 10;
            if (Integer.MAX_VALUE/10 < reversed || Integer.MAX_VALUE/10 == reversed && left > Integer.MAX_VALUE % 10) {
                return false;
            }
            reversed = reversed * 10 + left;
            x /= 10;
        }
        return reversed == x;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,3,6,7};
        System.out.println(new Solution().isPalindrome(121));

    }
}