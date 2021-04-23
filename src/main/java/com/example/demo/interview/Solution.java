package com.example.demo.interview;

public class Solution {

    public int maxLengthOfATree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxLengthOfATree(root.left), maxLengthOfATree(root.right)) + 1;
    }
}

class TreeNode {
    public int val;

    public TreeNode left;

    public TreeNode right;
}
