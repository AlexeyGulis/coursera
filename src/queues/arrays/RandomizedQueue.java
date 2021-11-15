package queues.arrays;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private final static int INITIAL_CAPACITY = 10;
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
        System.arraycopy(items, numberOut + 1, items, numberOut, size - numberOut - 1);
        items[--size] = null;
        if (size * 4 <= items.length) {
            resize(false);
        }
        return result;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int numberOut = StdRandom.uniform(size);
        Item result = items[numberOut];
        return result;
    }

    public Iterator<Item> iterator(){
        return new CustomIterator<>();
    }
    private class CustomIterator<Item> implements Iterator<Item>{
        private Item[] itemsShuffl = (Item[]) new Object[size];
        private int count = 0;
        CustomIterator(){
            System.arraycopy(items,0,itemsShuffl,0,size);
            StdRandom.shuffle(itemsShuffl);
        }
        @Override
        public boolean hasNext() {
            return count < itemsShuffl.length;
        }

        @Override
        public Item next() {
            return itemsShuffl[count++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Illegal operation");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.size();
        randomizedQueue.enqueue("it ");
        randomizedQueue.enqueue("was ");
        randomizedQueue.enqueue("amazing ");
        randomizedQueue.enqueue("amazing12 ");
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
    }
}
