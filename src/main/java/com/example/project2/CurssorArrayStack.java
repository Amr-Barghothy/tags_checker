package com.example.project2;

public class CurssorArrayStack<T extends Comparable<T>> implements Stack_Interface<T> {

    CursorArray<T> cursorArray;
    int cs;

    CurssorArrayStack(int capacity) {
        cursorArray = new CursorArray<>(capacity);
        cs = cursorArray.createList();
    }


    @Override
    public void push(T data) {
        if (cursorArray.hasFree())
            cursorArray.insertAtHead(data, cs);
        else
            System.out.println("Stack Overflow!");

    }

    @Override
    public T pop() {
        if (!cursorArray.isEmpty(cs))
            return cursorArray.deleteFirst(cs);
        return null;
    }

    @Override
    public T peek() {
        return cursorArray.seeFirst(cs);
    }

    @Override
    public boolean isEmpty() {

        return cursorArray.isEmpty(cs);
    }

    @Override
    public void clear() {
    }




    public String toString(){
        return cursorArray.toString();
    }



}
