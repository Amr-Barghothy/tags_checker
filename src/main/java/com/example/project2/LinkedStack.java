package com.example.project2;

import java.util.Iterator;

public class LinkedStack<T extends Comparable<T>> implements Iterable<T> {
    private Node<T> topNode;

    public void push(T data) {
        Node<T> newNode = new Node<T>(data);
        newNode.setNext(topNode);
        topNode = newNode;
    }

    public Node<T> pop() {
        Node<T> toDel = topNode;
        if (topNode != null)
            topNode = topNode.getNext();
        return toDel;
    }

    public Node<T> peek() {
        return topNode;
    }

    public int length() {
        int length = 0;
        Node<T> curr = topNode;
        while (curr != null) {
            length++;
            curr = curr.getNext();
        }
        return length;
    }

    public boolean isEmpty() {
        return (topNode == null);
    }

    public void clear() {
        topNode = null;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<T> {
        private Node<T> curr = topNode;

        public boolean hasNext() {
            return curr != null;
        }

        public void remove() {
        }

        public T next() {
            T t = curr.getData();
            curr = curr.getNext();
            return t;
        }
    }

    @Override
    public String
    toString() {
        return "LinkedStack{" +
                "topNode=" + topNode +
                '}';
    }
}


