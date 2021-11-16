package queues.arrays;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_CAPACITY = 10;
    private int size = 0;
    private Item[] items;

    public RandomizedQueue() {
        items = (Item[]) new Object[INITIAL_CAPACITY];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(boolean flag) {
        int newCapacity;
        if (flag) {
            newCapacity = items.length * 2;
        } else {
            newCapacity = items.length / 2;
        }
        Item[] temp = (Item[]) new Object[newCapacity];
        System.arraycopy(items, 0, temp, 0, size);
        items = temp;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == items.length) {
            resize(true);
        }
        items[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int numberOut = StdRandom.uniform(size);
        Item result = items[numberOut];
        items[numberOut] = items[size - 1];
        items[--size] = null;
        if (size * 4 <= items.length && size * 4 > INITIAL_CAPACITY) {
            resize(false);
        }
        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int numberOut = StdRandom.uniform(size);
        return items[numberOut];
    }

    public Iterator<Item> iterator() {
        return new CustomIterator<>();
    }

    private class CustomIterator<Item> implements Iterator<Item> {
        private final Item[] itemsShuffle;
        private int count = 0;

        public CustomIterator() {
            itemsShuffle = (Item[]) new Object[size()];
            System.arraycopy(items, 0, itemsShuffle, 0, size());
            StdRandom.shuffle(itemsShuffle);
        }

        @Override
        public boolean hasNext() {
            return count < itemsShuffle.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return itemsShuffle[count++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Illegal operation");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue("it");
        randomizedQueue.enqueue("was");
        randomizedQueue.enqueue("amazing");
        randomizedQueue.enqueue("amazing12");
        StdOut.println(randomizedQueue.size());
        StdOut.println(randomizedQueue.dequeue());
        StdOut.println(randomizedQueue.sample());
        for (String str : randomizedQueue
        ) {
            for (String str1 : randomizedQueue
            ) {
                StdOut.print(str + " + " + str1 + " ");
            }
            StdOut.println();
        }
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
        randomizedQueue.enqueue("it");
        randomizedQueue.enqueue("was");
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
        randomizedQueue.enqueue("was");
        randomizedQueue.dequeue();
    }
}
