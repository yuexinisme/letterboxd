package com.example.demo.concurrent;

import java.util.Stack;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;

public class MyStack<T> {

    private class Node<T> {
        private T val;

        private Node<T> next;

        private Node<T> pre;

        Node() {

        }

        Node(T val) {
            this.val = val;
        }
    }

    private Node<T> head;

    private Node<T> tail;

    public void push(T val) {
        Node<T> node = new Node<>(val);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.pre = tail;
            tail = node;
        }
    }

    public T pop() {
        if (tail == null) {
            return null;
        }
        T val = tail.val;
        Node<T> pre = tail.pre;
        if (pre == null) {
            head = null;
            tail = null;
            return val;
        }
        pre.next = null;
        tail = pre;
        return val;
    }

    @Override
    public String toString() {
        Node<T> node = head;
        StringBuilder sb = new StringBuilder();
        while (node != null) {
            sb.append(node.val);
            if (node.next != null) {
                sb.append("->");
            }
            node = node.next;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        MyStack<String> ms = new MyStack();
        ms.push("gaga");
        ms.push("kubo");
        ms.push("gaga");
        ms.push("xx");
        System.out.println(ms.pop());
        System.out.println(ms);
        System.out.println(ms.pop());
        System.out.println(ms);
        System.out.println(ms.pop());
        System.out.println(ms);
        System.out.println(ms.pop());
        System.out.println(ms);
        Stack s;
    }
}
