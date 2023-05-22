package com.example.project2;

public interface Stack_Interface<T> {
    public void push(T data);

    public T pop();

    public T peek();

    public boolean isEmpty();

    public void clear();
}
