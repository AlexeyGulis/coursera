package queues.arrays;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*Лучшее решение для двусторонней очереди будет связанный список,
копирование элементов после удаления 1 ого будет занимать не постоянное время*/


public class Deque<Item> implements Iterable<Item> {

    private int head = 0;
    private int tail = 0;
    private Item[] items;

    public Deque() {
        items = (Item[]) new Object[6];
    }

    public boolean isEmpty() {
        return size() == 0 ? true : false;
    }

    public int size() {
        return tail - head;
    }

    private void resize(int k) {
        if (k == 1) {
            Item[] temp = (Item[]) new Object[items.length * 2];
            System.arraycopy(items, 0, temp, 1, items.length);
            items = temp;
        } else if (k == 2) {
            Item[] temp = (Item[]) new Object[items.length * 2];
            System.arraycopy(items, 0, temp, 0, items.length);
            items = temp;
        } else {
            Item[] temp = (Item[]) new Object[items.length / 2];
            System.arraycopy(items, head, temp, 0, tail - head);
            tail = tail - head;
            head = 0;
            items = temp;
        }
    }

    private void checkArray() {
        if (head != 0 && ((tail - head) * 2 <= items.length || tail == items.length)) {
            Item[] temp = (Item[]) new Object[items.length];
            System.arraycopy(items, head, temp, 0, tail - head);
            tail = tail - head;
            head = 0;
            items = temp;
        }
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (head != 0) {
            items[--head] = item;
            checkArray();
        } else {
            if (tail == items.length) {
                resize(1);
            }
            items[head] = item;
            tail++;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        checkArray();
        if (tail == items.length) {
            resize(2);
        }
        items[tail++] = item;
    }

    public void removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        items[head++] = null;
        if ((tail - head) * 4 == items.length) {
            resize(3);
        }
    }

    public void removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        items[--tail] = null;
        if ((tail - head) * 4 == items.length) {
            resize(3);
        }
    }

    @Override
    public Iterator<Item> iterator() {
        Iterator<Item> iterator = new Iterator<Item>() {
            private int count = head;

            @Override
            public boolean hasNext() {
                return count < tail;
            }

            @Override
            public Item next() {
                return items[count++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Illegal operation");
            }
        };
        return iterator;
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(3);
        deque.addLast(2);
        deque.addLast(2);
        deque.addLast(2);
        deque.addLast(2);
        deque.removeFirst();
        deque.addLast(1);
        deque.removeLast();
        Iterator item = deque.iterator();
        while (item.hasNext()){
            System.out.println(item.next());
        }
    }
}


