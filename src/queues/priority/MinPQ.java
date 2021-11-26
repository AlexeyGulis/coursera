package queues.priority;

import edu.princeton.cs.algs4.StdOut;

public class MinPQ<Key extends Comparable<Key>> {
    private int size;
    private static final int INITIAL_CAPACITY = 10;
    private Key[] a;

    public MinPQ() {
        a = (Key[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    public MinPQ(Key[] a) {
        if (a != null) {
            System.arraycopy(a,0,this.a,0,a.length);
        }
        size = a.length;
    }

    public void insert(Integer k) {
        if (k == null) {
            StdOut.println("Invalid arg");
            return;
        }
    }

    public Integer deleteMin() {
        return null;
    }

    private void resize() {

    }

    public int size() {
        return 0;
    }
}
