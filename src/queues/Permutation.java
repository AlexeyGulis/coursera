package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import queues.arrays.RandomizedQueue;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int n = 0;
        while (!StdIn.isEmpty() && k != 0) {
            if (n >= k) {
                randomizedQueue.dequeue();
            }
            randomizedQueue.enqueue(StdIn.readString());
            n++;
        }
        for (String str : randomizedQueue
        ) {
            StdOut.println(str);
        }
    }
}
