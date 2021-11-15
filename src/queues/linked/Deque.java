package queues.linked;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*Лучшее решение для двусторонней очереди будет связанный список,
копирование элементов после удаления 1 ого будет занимать не постоянное время*/


public class Deque<Item> implements Iterable<Item> {

    private Node head = null;
    private Node tail = null;
    private int count = 0;

    class Node {
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

    public void removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (head.next == tail) {
            head = tail;
            head.next = null;
        } else {
            head = head.next;
        }
        head.prev = null;
    }

    public void removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (head == tail.prev) {
            head = tail;
            tail.prev = null;
        } else {

            tail = tail.prev;
        }
        tail.next = null;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                Node temp = current;
                current = current.next;
                return temp.item;
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
        for (Integer integer : deque
        ) {
            System.out.println(integer);
        }
    }
}
