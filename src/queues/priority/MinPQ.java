package queues.priority;

import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class MinPQ<Key> implements Iterable<Key> {
    private int size;
    private Key[] a;
    private Comparator<Key> comparator;

    public MinPQ() {
        this(1);
    }

    public MinPQ(int initCapacity){
        a = (Key[]) new Object[initCapacity + 1];
        size = 0;
    }

    public MinPQ(Key[] array) {
        this(array.length);
        for (Key b : array
             ) {
            this.insert(b);
        }
    }

    public MinPQ(Key[] array, Comparator<Key> comparator) {
        this(array.length);
        this.comparator = comparator;
        for (Key b : array
        ) {
            this.insert(b);
        }
    }

    public void insert(Key k) {
        if(size == a.length - 1){
            resize(2 * a.length);
        }
        a[++size] = k;
        swim(size);
    }

    private void resize(int capacity){
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i < a.length; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    private void swim(int size){
        while (size > 1){
        }
    }

    private boolean greater(int i, int j){
        return false;
    }

    public Integer deleteMin() {
        return null;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size() {
        return size;
    }


    @Override
    public Iterator<Key> iterator() {
        return null;
    }
}
