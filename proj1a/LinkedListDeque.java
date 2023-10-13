public class LinkedListDeque<T> {
    private class Deque {
        private Deque prev;
        private T item;
        private Deque next;

        private Deque(Deque p, T i, Deque n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private Deque sentFront;
    private Deque sentBack;
    private int size;



    public LinkedListDeque() {
        sentFront = new Deque(null, (T) new Object(), null);
        sentBack = sentFront;
        size = 0;
    }

    public void addFirst(T item) {
        sentFront.next = new Deque(sentFront, item, sentFront.next);
        size += 1;
        if (size == 1) {
            sentBack = sentFront.next;
        }
    }

    public void addLast(T item) {
        sentBack.prev = new Deque(sentBack.prev, item, sentBack);
        size += 1;
        if (size == 1) {
            sentFront = sentBack.prev;
        }
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Deque ptr = sentFront;
        while (ptr.next != null) {
            ptr = ptr.next;
            System.out.print(ptr.item);
            System.out.print(" ");
        }
        System.out.println(" ");
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentFront.next;
        if (removedeque.next == sentBack) {
            sentFront = sentBack;
        }
        sentFront.next = removedeque.next;
        size -= 1;
        return removedeque.item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Deque removedeque = sentBack.prev;
        if (removedeque.prev == sentFront) {
            sentBack = sentFront;
        }
        sentBack.prev = removedeque.prev;
        size -= 1;
        return removedeque.item;
    }

    public T get(int index) {
        if (size <= index || size == 0) {
            return null;
        }
        Deque ptr = sentFront.next;
        int a = 0;
        while (a < index) {
            a = a + 1;
            ptr = ptr.next;
        }
        return ptr.item;
    }

    public T getRecursive(int index) {
        return helper(index, sentFront.next);
    }

    private T helper(int index, Deque newdeque) {
        if (index >= size || size == 0) {
            return null;
        }
        if (index == 0) {
            return newdeque.item;
        }
        return helper(index - 1, newdeque.next);
    }
}
