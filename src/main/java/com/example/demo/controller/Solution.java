package com.example.demo.controller;

import java.sql.SQLOutput;
import java.util.List;

class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode temp = this;
        while (temp != null) {
            int v = temp.val;
            sb.append(v + "->");
            temp = temp.next;
        }
        return sb.toString();
    }
}

public class Solution {
    public static void main (String[] args) {
        ListNode n1 = new ListNode(9);
        ListNode t1 = n1;
        ListNode n2 = new ListNode(9);
        ListNode t2 = n2;
        int times = 0;
        while (times < 6) {
            n1.next = new ListNode(9);
            n1 = n1.next;
            if (times < 3) {
                n2.next = new ListNode(9);
                n2 = n2.next;
            }
            times++;
        }
        System.out.println("part1");
        System.out.println(t1);
        System.out.println(t2);
        ListNode x = new Solution().addTwoNumbers(t1, t2);
        System.out.println(x);
    }
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = new ListNode();
        ListNode temp = head;
        boolean plus = false;
        while(l1 != null && l2 != null) {
            ListNode newNode = new ListNode();
            int NewVal = l1.val + l2.val;
            if (plus) {
                NewVal += 1;
            }
            if (NewVal > 9) {
                NewVal %= 10;
                plus = true;
            } else {
                plus = false;
            }
            newNode.val = NewVal;
            head.next = newNode;
            head = newNode;
            l1 = l1.next;
            l2 = l2.next;
        }
        if (l1 == null) {
            head.next = l2;
        } else {
            head.next = l1;
        }
        if (head.next != null) {
            head = head.next;
            while (head.next != null) {
                int v = head.val;
                if (plus) {
                    v += 1;
                }
                if (v > 9) {
                    plus = true;
                    v %= 10;
                } else {
                    plus = false;
                }
                head.val = v;
                head = head.next;
            }
        }

        if (plus) {
            head.next = new ListNode(1);
        }
        return temp.next;
    }


}