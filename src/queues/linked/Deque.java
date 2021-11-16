package queues.linked;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int count = 0;

    private class Node {
        public Item item;
        public Node prev;
        public Node next;

        public Node(Item item) {
            this.item = item;
        }
    }

    public Deque() {
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return count;
    }


    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            head = new Node(item);
            tail = head;

        } else {
            Node newItem = new Node(item);
            newItem.next = head;
            head.prev = newItem;
            head = newItem;
        }
        count++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            head = new Node(item);
            tail = head;

        } else {
            Node newItem = new Node(item);
            newItem.prev = tail;
            tail.next = newItem;
            tail = newItem;
        }
        count++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item temp = head.item;
        if (head.next == tail) {
            head = tail;
            head.next = null;
            head.prev = null;
        } else if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        count--;
        return temp;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item temp = tail.item;
        if (tail.prev == head) {
            tail = head;
            tail.prev = null;
            tail.next = null;
        } else if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        count--;
        return temp;
    }

    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Item temp = current.item;
                current = current.next;
                return temp;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Illegal operation");
            }
        };
    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(3);
        deque.addFirst(2);
        deque.addLast(4);
        deque.removeLast();
        deque.addFirst(5);
        deque.removeFirst();
        deque.addLast(6);
        System.out.println(deque.size());
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.removeLast();
        deque.addFirst(1);
        deque.addLast(3);
        deque.addFirst(2);
        deque.addLast(4);
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
        for (Integer integer : deque
        ) {
            for (Integer integer1 : deque
            ) {
                StdOut.print(integer + " " + integer1);
            }
            StdOut.println();
        }
    }
}
