package com.example.project2;

public class CursorArray<T extends Comparable<T>> {
    @SuppressWarnings("unchecked")
    CNode<T>[] cursorArray = new CNode[11];

    @SuppressWarnings("unchecked")
    public CursorArray(int capacity) {
        cursorArray = new CNode[capacity];
        initialization();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public int createList() {
        int l = malloc();
        if (l == 0)
            System.out.println("Error: Out of space!!!");
        else
            cursorArray[l] = new CNode("-", 0);
        return l;
    }

    public int initialization() {
        for (int i = 0; i < cursorArray.length - 1; i++)
            cursorArray[i] = new CNode<>(null, i + 1);
        cursorArray[cursorArray.length - 1] = new CNode<>(null, 0);
        return 0;
    }

    private int malloc() {
        int p = cursorArray[0].getNext();
        cursorArray[0].setNext(cursorArray[p].getNext());
        return p;
    }

    public void free(int p) {
        cursorArray[p] = new CNode<>(null, cursorArray[0].getNext());
        cursorArray[0].setNext(p);
    }

    private boolean isNull(int l) {
        return cursorArray[l] == null;
    }

    public boolean isEmpty(int l) {
        return cursorArray[l].getNext() == 0;
    }

    public boolean isLast(int p) {
        return cursorArray[p].getNext() == 0;
    }

    public void insertAtHead(T data, int l) {
        if (isNull(l)) // list not created
            return;
        int p = malloc();
        if (p != 0) {
            cursorArray[p] = new CNode<>(data, cursorArray[l].getNext());
            cursorArray[l].setNext(p);
        } else
            System.out.println("Error: Out of space!!!");
    }

    public void traversList(int l) {
        System.out.print("list_" + l + "-->");
        while (!isNull(l) && !isEmpty(l)) {
            l = cursorArray[l].getNext();
            System.out.print(cursorArray[l] + "-->");
        }
        System.out.println("null");
    }

    public int find(T data, int l) {
        while (!isNull(l) && !isEmpty(l)) {
            l = cursorArray[l].getNext();
            if (cursorArray[l].getData().equals(data))
                return l;
        }
        return -1; // not found
    }

    public int findPrevious(T data, int l) {
        while (!isNull(l) && !isEmpty(l)) {
            if (cursorArray[cursorArray[l].getNext()].getData().equals(data))
                return l;
            l = cursorArray[l].getNext();
        }
        return -1; // not found
    }

    public int recFindPrevious(T data, int l) {
        if (!isNull(l) && !isEmpty(l)) {
            if (cursorArray[cursorArray[l].getNext()].getData().equals(data))
                return l;
            return recFindPrevious(data, cursorArray[l].getNext());
        }
        return -1; // not found
    }

    public CNode<T> delete(T data, int l) {
        int p = findPrevious(data, l);
        if (p != -1) {
            int c = cursorArray[p].getNext();
            CNode<T> temp = cursorArray[c];
            cursorArray[p].setNext(temp.getNext());
            free(c);
        }
        return null;
    }

    public T deleteFirst(int L){
        if(isNull(L))
            return null;
        if(!isEmpty(L)){
            int next = cursorArray[L].getNext();
            T t = cursorArray[next].getData();
            cursorArray[L].setNext(cursorArray[next].getNext());
            free(next);
            return t;

        }
        return null;

    }

    public void insertAtLast(T data, int l) {
        if (isNull(l))
            return;
        int p = malloc();
        if (p != 0) {
            int index = l;
            while (!isNull(index) && !isLast(index))
                index = cursorArray[index].getNext();
            cursorArray[index].setNext(p);
            cursorArray[p] = new CNode<>(data, 0);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < cursorArray.length; i++)
            s += i + ": " + cursorArray[i] + "\n";

        return s;
    }
    public boolean hasFree(){
        return cursorArray[0].getNext()!=0;
    }

    public T seeFirst(int l) {
        if(isNull(l))
            return null;
        if(!isEmpty(l)){
            int next = cursorArray[l].getNext();
            T t = cursorArray[next].getData();
            return t;
        }
        return null;
    }


//********************************************************************************************************* insertSorted


}
