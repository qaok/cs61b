public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        private Node prev;
        private T item;
        private Node next;

        private Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
            if (n != null) {
                n.prev = this;
            }
            if (p != null) {
                p.next = this;
            }
        }
    }

    private Node sentFront;
    private Node sentBack;
    private int size;


    public LinkedListDeque() {
        sentFront = new Node(null, (T) new Object(), null);
        sentBack = sentFront;
        size = 0;
    }
    
    @Override
    public void addFirst(T item) {
        Node newdeque = new Node(sentFront, item, sentFront.next);
        size += 1;
        if (size == 1) {
            sentBack = newdeque;
        }
    }
    
    @Override
    public void addLast(T item) {
        Node newdeque = new Node(sentBack, item, null);
        sentBack = newdeque;
        size += 1;
    }
    
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void printDeque() {
        Node ptr = sentFront;
        while (ptr.next != null) {
            ptr = ptr.next;
            System.out.print(ptr.item);
            System.out.print(" ");
        }
        System.out.println(" ");
    }
    
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node removedeque = sentFront.next;
        sentFront.next = removedeque.next;
        if (removedeque == sentBack) {
            sentBack = sentFront;
        } else {
            removedeque.next.prev = sentFront;
        }
        size -= 1;
        return removedeque.item;
    }
    
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node removedeque = sentBack;
        sentBack = removedeque.prev;
        sentBack.next = null;
        size -= 1;
        return removedeque.item;
    }
    
    @Override
    public T get(int index) {
        if (size <= index || size == 0) {
            return null;
        }
        Node ptr = sentFront.next;
        int a = 0;
        while (a < index) {
            a = a + 1;
            ptr = ptr.next;
        }
        return ptr.item;
    }
    
    private T helper(int index, Node nowdeque) {
        if (index == 0) {
            return nowdeque.item;
        }
        if (index >= size || size == 0) {
            return null;
        }
        return helper(index - 1, nowdeque.next);
    }

    public T getRecursive(int index) {
        return helper(index, sentFront.next);
    }
    
}
